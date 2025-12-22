package com.servify.payment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentIntentResponse {
    private String clientSecret;
    private String paymentIntentId;
    private Long amount;
    private String currency;
}
