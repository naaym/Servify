package com.servify.payment.service;

import com.servify.booking.model.BookingEntity;
import com.servify.booking.repository.BookingRepository;
import com.servify.client.model.ClientEntity;
import com.servify.client.repository.ClientRepository;
import com.servify.payment.dto.PaymentHistoryItemResponse;
import com.servify.payment.model.PaymentTransaction;
import com.servify.payment.repository.PaymentTransactionRepository;
import com.servify.provider.model.ProviderEntity;
import com.servify.provider.repository.ProviderRepository;
import com.servify.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentHistoryService {

    private final PaymentTransactionRepository transactionRepository;
    private final BookingRepository bookingRepository;
    private final ClientRepository clientRepository;
    private final ProviderRepository providerRepository;

    public List<PaymentHistoryItemResponse> getClientHistory() {
        ClientEntity client = clientRepository.findByEmail(getCurrentUserEmail())
            .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
        List<BookingEntity> bookings = bookingRepository.findByClientUserIdOrderByCreatedAtDesc(client.getUserId());
        return mapHistoryForBookings(bookings);
    }

    public List<PaymentHistoryItemResponse> getProviderHistory() {
        ProviderEntity provider = providerRepository.findByEmail(getCurrentUserEmail())
            .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));
        List<BookingEntity> bookings = bookingRepository.findByProviderUserIdOrderByCreatedAtDesc(provider.getUserId());
        return mapHistoryForBookings(bookings);
    }

    private List<PaymentHistoryItemResponse> mapHistoryForBookings(List<BookingEntity> bookings) {
        if (bookings == null || bookings.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> bookingIds = bookings.stream()
            .map(BookingEntity::getBookingId)
            .toList();
        Map<Long, BookingEntity> bookingMap = bookings.stream()
            .collect(Collectors.toMap(BookingEntity::getBookingId, Function.identity()));

        return transactionRepository.findByOrderIdInOrderByCreatedAtDesc(bookingIds).stream()
            .map(transaction -> toResponse(transaction, bookingMap.get(transaction.getOrderId())))
            .toList();
    }

    private PaymentHistoryItemResponse toResponse(PaymentTransaction transaction, BookingEntity booking) {
        String providerName = booking != null && booking.getProvider() != null
            ? booking.getProvider().getName()
            : null;
        String clientName = booking != null && booking.getClient() != null
            ? booking.getClient().getName()
            : null;

        return PaymentHistoryItemResponse.builder()
            .bookingId(transaction.getOrderId())
            .paymentIntentId(transaction.getPaymentIntentId())
            .amount(transaction.getAmount())
            .currency(transaction.getCurrency())
            .status(transaction.getStatus())
            .createdAt(transaction.getCreatedAt())
            .providerName(providerName)
            .clientName(clientName)
            .bookingDate(booking != null ? booking.getCreatedAt() : null)
            .build();
    }

    private String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
