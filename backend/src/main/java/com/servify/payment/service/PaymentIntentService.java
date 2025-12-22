package com.servify.payment.service;

import com.servify.payment.config.StripeProperties;
import com.servify.payment.dto.PaymentIntentResponse;
import com.servify.payment.model.PaymentStatus;
import com.servify.payment.model.PaymentTransaction;
import com.servify.payment.repository.PaymentTransactionRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentIntentService {

    private final StripeProperties stripeProperties;
    private final PaymentTransactionRepository transactionRepository;
    private final PaymentOrderService paymentOrderService;
    private final PaymentAmountCalculator paymentAmountCalculator;

    public PaymentIntentResponse createPaymentIntent(Long orderId) throws StripeException {
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
            .build();

        RequestOptions requestOptions = RequestOptions.builder()
            .setIdempotencyKey("order-" + orderId)
            .build();

        PaymentIntent intent = PaymentIntent.create(params, requestOptions);
        persistTransaction(orderId, intent.getId(), minorAmount, currency);

        return PaymentIntentResponse.builder()
            .clientSecret(intent.getClientSecret())
            .paymentIntentId(intent.getId())
            .amount(minorAmount)
            .currency(currency)
            .build();
    }

    private void persistTransaction(Long orderId, String paymentIntentId, long amount, String currency) {
        PaymentTransaction transaction = transactionRepository.findByPaymentIntentId(paymentIntentId)
            .orElseGet(PaymentTransaction::new);
        transaction.setOrderId(orderId);
        transaction.setPaymentIntentId(paymentIntentId);
        transaction.setAmount(amount);
        transaction.setCurrency(currency);
        transaction.setStatus(PaymentStatus.PENDING);
        transactionRepository.save(transaction);
        log.info("PaymentIntent persisted for orderId={} intentId={}", orderId, paymentIntentId);
    }
}
