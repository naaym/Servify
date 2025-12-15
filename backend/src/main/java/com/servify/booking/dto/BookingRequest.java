package com.servify.booking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BookingRequest {

    @NotNull
    private Long providerId;

    private String serviceCategory;

    @NotNull
    @FutureOrPresent
    private LocalDate date;

    @NotNull
    private LocalTime time;

    @NotBlank
    @Size(max = 1000)
    private String description;

    private List<String> attachments = new ArrayList<>();
}
