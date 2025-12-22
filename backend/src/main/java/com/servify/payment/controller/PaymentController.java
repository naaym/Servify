package com.servify.payment.controller;

import com.servify.payment.config.StripeProperties;
import com.servify.payment.dto.PaymentConfigResponse;
import com.servify.payment.dto.PaymentIntentRequest;
import com.servify.payment.dto.PaymentIntentResponse;
import com.servify.payment.service.PaymentIntentService;
import com.stripe.exception.StripeException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentIntentService paymentIntentService;
    private final StripeProperties stripeProperties;

    @PostMapping("/intents")
    public ResponseEntity<PaymentIntentResponse> createIntent(@Valid @RequestBody PaymentIntentRequest request) throws StripeException {
        return ResponseEntity.ok(paymentIntentService.createPaymentIntent(request.getOrderId()));
    }

    @GetMapping("/config")
    public ResponseEntity<PaymentConfigResponse> getConfig() {
        return ResponseEntity.ok(
            PaymentConfigResponse.builder()
                .publishableKey(stripeProperties.getPublishableKey())
                .currency(stripeProperties.getCurrency())
                .defaultAmount(stripeProperties.getAmountDefault().toPlainString())
                .build()
        );
    }
}
