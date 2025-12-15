package com.servify.booking.service;

import com.servify.booking.dto.BookingRequest;
import com.servify.booking.dto.BookingResponse;
import com.servify.booking.model.BookingStatus;

import java.util.List;

public interface BookingService {
    BookingResponse createBooking(BookingRequest request);
    BookingResponse getBooking(Long id);
    List<BookingResponse> getClientBookings(BookingStatus status);
    List<BookingResponse> getProviderBookings(BookingStatus status);
    BookingResponse accept(Long id);
    BookingResponse reject(Long id);
    BookingResponse cancel(Long id);
}
