package com.servify.payment.controller;

import com.servify.payment.config.StripeProperties;
import com.servify.payment.service.PaymentWebhookService;
import com.servify.payment.service.StripeWebhookVerifier;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/webhooks/stripe")
@RequiredArgsConstructor
@Slf4j
public class StripeWebhookController {

    private final StripeProperties stripeProperties;
    private final StripeWebhookVerifier webhookVerifier;
    private final PaymentWebhookService paymentWebhookService;

    @PostMapping
    public ResponseEntity<String> handleWebhook(
        @RequestBody String payload,
        @RequestHeader("Stripe-Signature") String signatureHeader,
        HttpServletRequest request
    ) {
        try {
            Event event = webhookVerifier.verify(payload, signatureHeader, stripeProperties.getWebhookSecret());
            paymentWebhookService.handleEvent(event);
            return ResponseEntity.ok("ok");
        } catch (SignatureVerificationException ex) {
            log.warn("Invalid Stripe webhook signature: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }
    }
}
