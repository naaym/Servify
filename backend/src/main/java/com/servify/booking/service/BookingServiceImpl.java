package com.servify.booking.service;

import com.servify.booking.dto.BookingRequest;
import com.servify.booking.dto.BookingResponse;
import com.servify.booking.exception.ForbiddenBookingOperationException;
import com.servify.booking.exception.InvalidBookingStateException;
import com.servify.booking.mapper.BookingMapper;
import com.servify.booking.model.BookingEntity;
import com.servify.booking.model.BookingStatus;
import com.servify.booking.repository.BookingRepository;
import com.servify.shared.exception.ResourceNotFoundException;
import com.servify.user.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingResponse createBooking(BookingRequest request) {
        BookingEntity entity = bookingMapper.toEntity(request);
        entity.setClientId(getCurrentUserId());
        BookingEntity saved = bookingRepository.save(entity);
        return bookingMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getBooking(Long id) {
        BookingEntity booking = findBookingOrThrow(id);
        ensureOwnership(booking);
        return bookingMapper.toResponse(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getClientBookings(BookingStatus status) {
        Long clientId = getCurrentUserId();
        List<BookingEntity> bookings = status == null
            ? bookingRepository.findAllByClientId(clientId)
            : bookingRepository.findAllByClientIdAndStatus(clientId, status);
        return bookings.stream().map(bookingMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getProviderBookings(BookingStatus status) {
        Long providerId = getCurrentUserId();
        List<BookingEntity> bookings = status == null
            ? bookingRepository.findAllByProviderId(providerId)
            : bookingRepository.findAllByProviderIdAndStatus(providerId, status);
        return bookings.stream().map(bookingMapper::toResponse).toList();
    }

    @Override
    public BookingResponse accept(Long id) {
        BookingEntity booking = findBookingOrThrow(id);
        ensureProvider(booking);
        transitionFromPending(booking, BookingStatus.ACCEPTED);
        return bookingMapper.toResponse(booking);
    }

    @Override
    public BookingResponse reject(Long id) {
        BookingEntity booking = findBookingOrThrow(id);
        ensureProvider(booking);
        transitionFromPending(booking, BookingStatus.REJECTED);
        return bookingMapper.toResponse(booking);
    }

    @Override
    public BookingResponse cancel(Long id) {
        BookingEntity booking = findBookingOrThrow(id);
        ensureClient(booking);
        transitionFromPending(booking, BookingStatus.CANCELLED);
        return bookingMapper.toResponse(booking);
    }

    private BookingEntity findBookingOrThrow(Long id) {
        return bookingRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + id));
    }

    private void ensureOwnership(BookingEntity booking) {
        Long userId = getCurrentUserId();
        if (!booking.getClientId().equals(userId) && !booking.getProviderId().equals(userId)) {
            throw new ForbiddenBookingOperationException("You are not allowed to access this booking");
        }
    }

    private void ensureProvider(BookingEntity booking) {
        Long userId = getCurrentUserId();
        if (!booking.getProviderId().equals(userId)) {
            throw new ForbiddenBookingOperationException("You can only manage your own booking requests");
        }
    }

    private void ensureClient(BookingEntity booking) {
        Long userId = getCurrentUserId();
        if (!booking.getClientId().equals(userId)) {
            throw new ForbiddenBookingOperationException("You can only update your own booking requests");
        }
    }

    private void transitionFromPending(BookingEntity booking, BookingStatus nextStatus) {
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new InvalidBookingStateException("Only pending bookings can be updated");
        }
        booking.setStatus(nextStatus);
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserEntity userEntity) {
            return userEntity.getUserId();
        }
        throw new ForbiddenBookingOperationException("Unable to identify current user");
    }
}
