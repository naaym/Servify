package com.servify.paymentsconnect.controller;

import com.servify.payment.dto.PaymentIntentResponse;
import com.servify.paymentsconnect.dto.ConnectAccountLinkResponse;
import com.servify.paymentsconnect.dto.ConnectPaymentIntentRequest;
import com.servify.paymentsconnect.service.PaymentConnectService;
import com.stripe.exception.StripeException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments-connect")
@RequiredArgsConstructor
public class PaymentConnectController {

    private final PaymentConnectService paymentConnectService;

    @PostMapping("/providers/{providerId}/onboard")
    public ResponseEntity<ConnectAccountLinkResponse> onboardProvider(@PathVariable Long providerId) throws StripeException {
        return ResponseEntity.ok(paymentConnectService.createOrRefreshOnboarding(providerId));
    }

    @PostMapping("/intents")
    public ResponseEntity<PaymentIntentResponse> createIntent(@Valid @RequestBody ConnectPaymentIntentRequest request) throws StripeException {
        return ResponseEntity.ok(paymentConnectService.createPaymentIntent(request.getOrderId(), request.getProviderId()));
    }
}
