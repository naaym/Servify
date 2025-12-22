package com.servify.payment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentConfigResponse {
    private String publishableKey;
    private String currency;
    private String defaultAmount;
}
