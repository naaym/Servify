package com.servify.paymentsconnect.service;

import com.servify.payment.config.StripeProperties;
import com.servify.payment.dto.PaymentIntentResponse;
import com.servify.payment.model.PaymentStatus;
import com.servify.payment.service.PaymentAmountCalculator;
import com.servify.payment.service.PaymentOrderService;
import com.servify.paymentsconnect.config.StripeConnectProperties;
import com.servify.paymentsconnect.dto.ConnectAccountLinkResponse;
import com.servify.paymentsconnect.model.PaymentConnectTransaction;
import com.servify.paymentsconnect.model.ProviderStripeAccount;
import com.servify.paymentsconnect.repository.PaymentConnectTransactionRepository;
import com.servify.paymentsconnect.repository.ProviderStripeAccountRepository;
import com.servify.provider.model.ProviderEntity;
import com.servify.provider.repository.ProviderRepository;
import com.servify.shared.exception.ResourceNotFoundException;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Transfer;
import com.stripe.net.RequestOptions;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.TransferCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentConnectService {

    private final ProviderRepository providerRepository;
    private final ProviderStripeAccountRepository providerStripeAccountRepository;
    private final PaymentConnectTransactionRepository transactionRepository;
    private final StripeConnectProperties stripeConnectProperties;
    private final StripeProperties stripeProperties;
    private final PaymentOrderService paymentOrderService;
    private final PaymentAmountCalculator paymentAmountCalculator;

    public ConnectAccountLinkResponse createOrRefreshOnboarding(Long providerId) throws StripeException {
        ProviderEntity provider = providerRepository.findById(providerId)
            .orElseThrow(() -> new ResourceNotFoundException("Provider not found for id " + providerId));

        ProviderStripeAccount stripeAccount = providerStripeAccountRepository
            .findByProvider_UserId(providerId)
            .orElseGet(() -> createStripeAccount(provider));

        AccountLinkCreateParams params = AccountLinkCreateParams.builder()
            .setAccount(stripeAccount.getStripeAccountId())
            .setRefreshUrl(stripeConnectProperties.getRefreshUrl())
            .setReturnUrl(stripeConnectProperties.getReturnUrl())
            .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
            .build();

        AccountLink accountLink = AccountLink.create(params);
        return ConnectAccountLinkResponse.builder()
            .accountId(stripeAccount.getStripeAccountId())
            .url(accountLink.getUrl())
            .build();
    }

    public PaymentIntentResponse createPaymentIntent(Long orderId, Long providerId) throws StripeException {
        ProviderStripeAccount account = providerStripeAccountRepository.findByProvider_UserId(providerId)
            .orElseThrow(() -> new IllegalArgumentException("Provider is not onboarded to Stripe Connect"));

        BigDecimal amount = paymentOrderService.resolveAmount(orderId);
        long minorAmount = paymentAmountCalculator.toMinorUnits(amount);
        String currency = stripeProperties.getCurrency();

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
            .setAmount(minorAmount)
            .setCurrency(currency)
            .setAutomaticPaymentMethods(
                PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                    .setEnabled(true)
                    .build()
            )
            .putMetadata("orderId", String.valueOf(orderId))
            .putMetadata("providerId", String.valueOf(providerId))
            .build();

        RequestOptions requestOptions = RequestOptions.builder()
            .setIdempotencyKey("connect-order-" + orderId)
            .build();

        PaymentIntent intent = PaymentIntent.create(params, requestOptions);

        PaymentConnectTransaction transaction = new PaymentConnectTransaction();
        transaction.setOrderId(orderId);
        transaction.setProviderId(providerId);
        transaction.setStripeAccountId(account.getStripeAccountId());
        transaction.setPaymentIntentId(intent.getId());
        transaction.setAmount(minorAmount);
        transaction.setCurrency(currency);
        transaction.setStatus(PaymentStatus.PENDING);
        transactionRepository.save(transaction);

        return PaymentIntentResponse.builder()
            .clientSecret(intent.getClientSecret())
            .paymentIntentId(intent.getId())
            .amount(minorAmount)
            .currency(currency)
            .build();
    }

    public void handleWebhook(Event event) throws StripeException {
        switch (event.getType()) {
            case "payment_intent.succeeded" -> handlePaymentSucceeded(event);
            case "payment_intent.payment_failed" -> handlePaymentFailed(event);
            default -> log.info("Unhandled Stripe Connect event type {}", event.getType());
        }
    }

    private ProviderStripeAccount createStripeAccount(ProviderEntity provider) {
        try {
            AccountCreateParams params = AccountCreateParams.builder()
                .setType(AccountCreateParams.Type.EXPRESS)
                .setEmail(provider.getEmail())
                .setCapabilities(
                    AccountCreateParams.Capabilities.builder()
                        .setCardPayments(
                            AccountCreateParams.Capabilities.CardPayments.builder()
                                .setRequested(true)
                                .build()
                        )
                        .setTransfers(
                            AccountCreateParams.Capabilities.Transfers.builder()
                                .setRequested(true)
                                .build()
                        )
                        .build()
                )
                .build();

            Account account = Account.create(params);
            ProviderStripeAccount stripeAccount = new ProviderStripeAccount();
            stripeAccount.setProvider(provider);
            stripeAccount.setStripeAccountId(account.getId());
            return providerStripeAccountRepository.save(stripeAccount);
        } catch (StripeException ex) {
            throw new IllegalStateException("Unable to create Stripe Connect account", ex);
        }
    }

    private void handlePaymentSucceeded(Event event) throws StripeException {
        PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer()
            .getObject()
            .orElse(null);
        if (intent == null) {
            log.warn("Stripe Connect webhook missing payment intent payload");
            return;
        }
        PaymentConnectTransaction transaction = transactionRepository.findByPaymentIntentId(intent.getId())
            .orElse(null);
        if (transaction == null) {
            log.warn("No connect transaction found for intent {}", intent.getId());
            return;
        }
        if (transaction.getTransferId() != null) {
            log.info("Transfer already created for intent {}", intent.getId());
            return;
        }
        String chargeId = intent.getLatestCharge();
        if (chargeId == null) {
            log.warn("No charge id found for payment intent {}", intent.getId());
            return;
        }

        TransferCreateParams params = TransferCreateParams.builder()
            .setAmount(transaction.getAmount())
            .setCurrency(transaction.getCurrency())
            .setDestination(transaction.getStripeAccountId())
            .setSourceTransaction(chargeId)
            .build();

        RequestOptions requestOptions = RequestOptions.builder()
            .setIdempotencyKey("transfer-" + intent.getId())
            .build();

        Transfer transfer = Transfer.create(params, requestOptions);
        transaction.setTransferId(transfer.getId());
        transaction.setStatus(PaymentStatus.SUCCEEDED);
        transactionRepository.save(transaction);
        log.info("Transfer {} created for intent {}", transfer.getId(), intent.getId());
    }

    private void handlePaymentFailed(Event event) {
        PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer()
            .getObject()
            .orElse(null);
        if (intent == null) {
            log.warn("Stripe Connect webhook missing payment intent payload");
            return;
        }
        transactionRepository.findByPaymentIntentId(intent.getId())
            .ifPresent(transaction -> {
                transaction.setStatus(PaymentStatus.FAILED);
                transactionRepository.save(transaction);
            });
    }
}
