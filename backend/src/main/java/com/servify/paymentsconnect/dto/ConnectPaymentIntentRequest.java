package com.servify.paymentsconnect.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectPaymentIntentRequest {
    @NotNull
    private Long orderId;
    @NotNull
    private Long providerId;
}
