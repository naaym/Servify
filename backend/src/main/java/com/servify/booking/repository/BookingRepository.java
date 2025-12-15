package com.servify.booking.repository;

import com.servify.booking.model.BookingEntity;
import com.servify.booking.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    List<BookingEntity> findAllByClientId(Long clientId);
    List<BookingEntity> findAllByClientIdAndStatus(Long clientId, BookingStatus status);
    List<BookingEntity> findAllByProviderId(Long providerId);
    List<BookingEntity> findAllByProviderIdAndStatus(Long providerId, BookingStatus status);
}
