package com.servify.payment.service;

import com.servify.payment.model.PaymentStatus;
import com.servify.payment.model.PaymentTransaction;
import com.servify.payment.repository.PaymentTransactionRepository;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentWebhookService {

    private final PaymentTransactionRepository transactionRepository;

    public void handleEvent(Event event) {
        switch (event.getType()) {
            case "payment_intent.succeeded" -> handleSucceeded(event);
            case "payment_intent.payment_failed" -> handleFailed(event);
            default -> log.info("Unhandled Stripe event type {}", event.getType());
        }
    }

    private void handleSucceeded(Event event) {
        PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer()
            .getObject()
            .orElse(null);
        if (intent == null) {
            log.warn("Stripe webhook missing payment intent payload");
            return;
        }
        transactionRepository.findByPaymentIntentId(intent.getId())
            .ifPresent(transaction -> updateStatus(transaction, PaymentStatus.SUCCEEDED));
    }

    private void handleFailed(Event event) {
        PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer()
            .getObject()
            .orElse(null);
        if (intent == null) {
            log.warn("Stripe webhook missing payment intent payload");
            return;
        }
        transactionRepository.findByPaymentIntentId(intent.getId())
            .ifPresent(transaction -> updateStatus(transaction, PaymentStatus.FAILED));
    }

    private void updateStatus(PaymentTransaction transaction, PaymentStatus status) {
        transaction.setStatus(status);
        transactionRepository.save(transaction);
        log.info("Payment transaction {} updated to {}", transaction.getPaymentIntentId(), status);
    }
}
