package com.servify.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.servify.booking.dto.BookingStatusUpdateRequest;
import com.servify.booking.model.BookingStatus;
import com.servify.booking.service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@AutoConfigureMockMvc(addFilters = false)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    @Test
    void clientCannotUpdateBookingStatusToNonCancelled() throws Exception {
        BookingStatusUpdateRequest request = new BookingStatusUpdateRequest();
        request.setStatus(BookingStatus.ACCEPTED);

        mockMvc.perform(patch("/api/bookings/{id}/status", 15L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(bookingService);
    }
}
