package com.servify.payment.service;

import com.stripe.exception.SignatureVerificationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class StripeWebhookVerifierTest {

    private final StripeWebhookVerifier verifier = new StripeWebhookVerifier();

    @Test
    void rejectsInvalidSignature() {
        String payload = "{\"id\":\"evt_test\"}";
        String signature = "t=12345,v1=invalid";
        String secret = "whsec_test";

        assertThrows(SignatureVerificationException.class, () ->
            verifier.verify(payload, signature, secret)
        );
    }
}
