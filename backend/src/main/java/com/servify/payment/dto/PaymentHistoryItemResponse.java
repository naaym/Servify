package com.servify.payment.dto;

import com.servify.payment.model.PaymentStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class PaymentHistoryItemResponse {
    private Long bookingId;
    private String paymentIntentId;
    private Long amount;
    private String currency;
    private PaymentStatus status;
    private Instant createdAt;
    private String providerName;
    private String clientName;
    private Instant bookingDate;
}
