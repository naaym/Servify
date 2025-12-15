package com.servify.booking.dto;

import com.servify.booking.model.BookingStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
public class BookingResponse {
    private Long id;
    private Long providerId;
    private Long clientId;
    private String serviceCategory;
    private LocalDate date;
    private LocalTime time;
    private String description;
    private BookingStatus status;
    private List<String> attachments;
    private Instant createdAt;
    private Instant updatedAt;
}
