package com.servify.paymentsconnect.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "stripe.connect")
@Getter
@Setter
public class StripeConnectProperties {
    private String webhookSecret;
    private String refreshUrl;
    private String returnUrl;
}
