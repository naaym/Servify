package com.servify.booking.service;

import com.servify.booking.model.BookingEntity;
import com.servify.booking.model.BookingStatus;
import com.servify.booking.repository.BookingRepository;
import com.servify.client.model.ClientEntity;
import com.servify.client.repository.ClientRepository;
import com.servify.payment.repository.PaymentTransactionRepository;
import com.servify.provider.model.ProviderEntity;
import com.servify.provider.model.ProviderStatus;
import com.servify.provider.repository.ProviderRepository;
import com.servify.review.dto.ReviewRequest;
import com.servify.review.repository.ReviewRepository;
import com.servify.shared.exception.ResourceNotFoundException;
import com.servify.user.enums.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ProviderRepository providerRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private PaymentTransactionRepository paymentTransactionRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private ProviderEntity provider;

    @BeforeEach
    void setupSecurityContext() {
        provider = new ProviderEntity();
        provider.setUserId(5L);
        provider.setEmail("provider@test.com");
        provider.setName("Provider");
        provider.setPassword("secret");
        provider.setPhone("12345");
        provider.setGovernorate("tunis");
        provider.setDelegation("delegation");
        provider.setStatus(ProviderStatus.ACCEPTED);
        provider.setServiceCategory("plumbing");
        provider.setRole(Role.PROVIDER);

        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken(provider.getEmail(), "token")
        );
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void updateStatusAsProviderAllowsDoneAfterAcceptance() {
        BookingEntity booking = new BookingEntity();
        booking.setBookingId(10L);
        booking.setStatus(BookingStatus.ACCEPTED);
        booking.setProvider(provider);

        when(providerRepository.findByEmail(provider.getEmail())).thenReturn(Optional.of(provider));
        when(bookingRepository.findByBookingIdAndProviderUserId(booking.getBookingId(), provider.getUserId()))
                .thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(BookingEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = bookingService.updateStatusAsProvider(booking.getBookingId(), BookingStatus.DONE);

        assertThat(response.getStatus()).isEqualTo(BookingStatus.DONE);
        verify(bookingRepository).save(booking);
    }

    @Test
    void submitReviewFailsWhenBookingNotDone() {
        ClientEntity client = new ClientEntity();
        client.setUserId(4L);
        client.setEmail("client@test.com");
        client.setName("Client");
        client.setPassword("secret");
        client.setPhone("12345");
        client.setGovernorate("tunis");
        client.setRole(Role.CLIENT);

        BookingEntity booking = new BookingEntity();
        booking.setBookingId(9L);
        booking.setClient(client);
        booking.setProvider(provider);
        booking.setStatus(BookingStatus.PENDING);
        booking.setAppointmentDate(LocalDate.now());
        booking.setAppointmentTime(LocalTime.NOON);
        booking.setDescription("desc");

        when(clientRepository.findByEmail(client.getEmail())).thenReturn(Optional.of(client));
        when(bookingRepository.findByBookingIdAndClientUserId(booking.getBookingId(), client.getUserId()))
                .thenReturn(Optional.of(booking));

        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken(client.getEmail(), "token")
        );

        assertThrows(ResponseStatusException.class, () ->
                bookingService.submitReview(booking.getBookingId(), new ReviewRequest())
        );

        verify(reviewRepository, never()).save(any());
    }

    @Test
    void updateStatusAsProviderThrowsWhenBookingMissing() {
        when(providerRepository.findByEmail(provider.getEmail())).thenReturn(Optional.of(provider));
        when(bookingRepository.findByBookingIdAndProviderUserId(1L, provider.getUserId()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                bookingService.updateStatusAsProvider(1L, BookingStatus.ACCEPTED)
        );
    }
}
