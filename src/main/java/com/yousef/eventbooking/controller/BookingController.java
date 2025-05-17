package com.yousef.eventbooking.controller;

import com.yousef.eventbooking.dto.response.BookingPageResponseDTO;
import com.yousef.eventbooking.dto.response.BookingResponseDTO;
import com.yousef.eventbooking.exception.custom.InsufficientTicketsException;
import com.yousef.eventbooking.exception.custom.ResourceNotFoundException;
import com.yousef.eventbooking.exception.custom.UnauthorizedAccessException;
import com.yousef.eventbooking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/booking")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
@Tag(name = "Booking Management", description = "Endpoints for the users to manage their bookings.")

public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/all")
    @Operation(summary = "Get all bookings", description = "Retrieves the list of bookings made by the user.")
    public ResponseEntity<BookingPageResponseDTO> getBookings(@RequestHeader("Authorization") String token,
                                                                @RequestParam(defaultValue = "0") Integer pageNo,
                                                                @RequestParam(defaultValue = "5") Integer pageSize,
                                                                @RequestParam(defaultValue = "bookingId") String sortBy) throws ResourceNotFoundException {
        BookingPageResponseDTO  bookings = bookingService.getBookings(token, pageNo, pageSize, sortBy);
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @PostMapping("/book/{eventId}")
    @Operation(summary = "Book a ticket", description = "Allows the user to book a ticket for an event.")
    public ResponseEntity<BookingResponseDTO> bookTicket(@PathVariable Long eventId, @RequestHeader("Authorization") String token) throws ResourceNotFoundException, InsufficientTicketsException {
        BookingResponseDTO bookingResponse = bookingService.bookTicket(eventId, token);
        return new ResponseEntity<>(bookingResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/cancel/{bookingId}")
    @Operation(summary = "Cancel a ticket", description = "Allows the user to cancel a previously booked ticket.")
    public ResponseEntity<String> cancelTicket(@PathVariable Long bookingId, @RequestHeader("Authorization") String token) throws ResourceNotFoundException, UnauthorizedAccessException {
        bookingService.cancelTicket(bookingId, token);
        return new ResponseEntity<>("Booking cancelled successfully", HttpStatus.OK);
    }
}
