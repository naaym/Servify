package com.servify.booking.exception;

public class ForbiddenBookingOperationException extends RuntimeException {
    public ForbiddenBookingOperationException(String message) {
        super(message);
    }
}
