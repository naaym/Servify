package com.servify.payment.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class PaymentAmountCalculator {

    public long toMinorUnits(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount is required");
        }
        return amount.setScale(2, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100))
            .longValueExact();
    }
}
