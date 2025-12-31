package com.servify.booking.repository;

import com.servify.booking.model.BookingEntity;
import com.servify.booking.model.BookingStatus;
import com.servify.client.model.ClientEntity;
import com.servify.provider.model.ProviderEntity;
import com.servify.provider.model.ProviderStatus;
import com.servify.user.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void countsBookingsByStatusForClient() {
        ClientEntity client = persistClient("client@test.com");
        ProviderEntity provider = persistProvider("provider@test.com");

        persistBooking(client, provider, BookingStatus.PENDING);
        persistBooking(client, provider, BookingStatus.ACCEPTED);
        persistBooking(client, provider, BookingStatus.DONE);

        long total = bookingRepository.countByClientUserId(client.getUserId());
        long done = bookingRepository.countByClientUserIdAndStatus(client.getUserId(), BookingStatus.DONE);
        long pending = bookingRepository.countByClientUserIdAndStatus(client.getUserId(), BookingStatus.PENDING);

        assertThat(total).isEqualTo(3);
        assertThat(done).isEqualTo(1);
        assertThat(pending).isEqualTo(1);
    }

    private BookingEntity persistBooking(ClientEntity client, ProviderEntity provider, BookingStatus status) {
        BookingEntity booking = new BookingEntity();
        booking.setClient(client);
        booking.setProvider(provider);
        booking.setAppointmentDate(LocalDate.now());
        booking.setAppointmentTime(LocalTime.NOON);
        booking.setDescription("visit");
        booking.setStatus(status);
        return entityManager.persistAndFlush(booking);
    }

    private ClientEntity persistClient(String email) {
        ClientEntity client = new ClientEntity();
        client.setName("Client");
        client.setEmail(email);
        client.setPassword("secret");
        client.setPhone("12345");
        client.setGovernorate("tunis");
        client.setRole(Role.CLIENT);
        return entityManager.persistAndFlush(client);
    }

    private ProviderEntity persistProvider(String email) {
        ProviderEntity provider = new ProviderEntity();
        provider.setName("Provider");
        provider.setEmail(email);
        provider.setPassword("secret");
        provider.setPhone("67890");
        provider.setGovernorate("tunis");
        provider.setDelegation("delegation");
        provider.setStatus(ProviderStatus.ACCEPTED);
        provider.setServiceCategory("plumbing");
        provider.setRole(Role.PROVIDER);
        return entityManager.persistAndFlush(provider);
    }
}
