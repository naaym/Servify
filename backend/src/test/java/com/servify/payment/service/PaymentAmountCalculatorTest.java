package com.servify.payment.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentAmountCalculatorTest {

    private final PaymentAmountCalculator calculator = new PaymentAmountCalculator();

    @Test
    void convertsAmountToMinorUnits() {
        long minorUnits = calculator.toMinorUnits(new BigDecimal("30.00"));
        assertThat(minorUnits).isEqualTo(3000L);
    }
}
