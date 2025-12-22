package com.servify.payment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentIntentRequest {
    @NotNull
    private Long orderId;
}
