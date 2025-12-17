package com.servify.booking.model;

import lombok.ToString;

@ToString
public enum BookingStatus {
    PENDING,
    ACCEPTED,
    REJECTED,
    CANCELLED
}
