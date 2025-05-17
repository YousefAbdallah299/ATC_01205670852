package com.yousef.eventbooking.controller;

import com.yousef.eventbooking.dto.response.BookingPageResponseDTO;
import com.yousef.eventbooking.dto.response.BookingResponseDTO;
import com.yousef.eventbooking.service.BookingService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    public BookingControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBookings() {
        String token = "Bearer token";
        BookingPageResponseDTO responseDTO = new BookingPageResponseDTO(
                List.of(new BookingResponseDTO()),
                0,
                5,
                1L,
                1,
                false
        );
        when(bookingService.getBookings(token, 0, 5, "bookingId")).thenReturn(responseDTO);

        ResponseEntity<BookingPageResponseDTO> response = bookingController.getBookings(token, 0, 5, "bookingId");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseDTO, response.getBody());
        verify(bookingService).getBookings(token, 0, 5, "bookingId");
    }

    @Test
    void testBookTicket() {
        Long eventId = 1L;
        String token = "Bearer token";
        BookingResponseDTO responseDTO = new BookingResponseDTO();
        when(bookingService.bookTicket(eventId, token)).thenReturn(responseDTO);

        ResponseEntity<BookingResponseDTO> response = bookingController.bookTicket(eventId, token);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(responseDTO, response.getBody());
        verify(bookingService).bookTicket(eventId, token);
    }

    @Test
    void testCancelTicket() {
        Long bookingId = 1L;
        String token = "Bearer token";

        ResponseEntity<String> response = bookingController.cancelTicket(bookingId, token);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Booking cancelled successfully", response.getBody());
        verify(bookingService).cancelTicket(bookingId, token);
    }
}
