package com.servify.payment.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@ConfigurationProperties(prefix = "stripe")
@Getter
@Setter
public class StripeProperties {
    private String secretKey;
    private String webhookSecret;
    private String publishableKey;
    private String currency = "eur";
    private BigDecimal amountDefault = new BigDecimal("30.00");
}
