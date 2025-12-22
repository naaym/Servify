package com.servify.paymentsconnect.controller;

import com.servify.payment.service.StripeWebhookVerifier;
import com.servify.paymentsconnect.config.StripeConnectProperties;
import com.servify.paymentsconnect.service.PaymentConnectService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
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
@RequestMapping("/api/payments-connect/webhooks/stripe")
@RequiredArgsConstructor
@Slf4j
public class StripeConnectWebhookController {

    private final StripeConnectProperties stripeConnectProperties;
    private final StripeWebhookVerifier webhookVerifier;
    private final PaymentConnectService paymentConnectService;

    @PostMapping
    public ResponseEntity<String> handleWebhook(
        @RequestBody String payload,
        @RequestHeader("Stripe-Signature") String signatureHeader
    ) {
        try {
            Event event = webhookVerifier.verify(payload, signatureHeader, stripeConnectProperties.getWebhookSecret());
            paymentConnectService.handleWebhook(event);
            return ResponseEntity.ok("ok");
        } catch (SignatureVerificationException ex) {
            log.warn("Invalid Stripe Connect webhook signature: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        } catch (StripeException ex) {
            log.error("Stripe Connect webhook processing error", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Webhook error");
        }
    }
}
