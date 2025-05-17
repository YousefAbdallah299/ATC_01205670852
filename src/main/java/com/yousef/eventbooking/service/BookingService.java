package com.yousef.eventbooking.service;

import com.yousef.eventbooking.dto.response.BookingPageResponseDTO;
import com.yousef.eventbooking.dto.response.BookingResponseDTO;
import com.yousef.eventbooking.exception.custom.EventAlreadyBookedException;
import com.yousef.eventbooking.exception.custom.InsufficientTicketsException;
import com.yousef.eventbooking.exception.custom.ResourceNotFoundException;
import com.yousef.eventbooking.exception.custom.UnauthorizedAccessException;

import java.util.List;

public interface BookingService {
    /**
     * Books a ticket for an event.
     *
     * @param eventId the ID of the event to book
     * @param token   the JWT token of the user making the request
     * @return a message indicating the success of the booking
     */
    BookingResponseDTO bookTicket(Long eventId, String token) throws ResourceNotFoundException, InsufficientTicketsException, EventAlreadyBookedException;

    /**
     * Cancels a booked ticket for an event.
     *
     * @param eventId the ID of the event to cancel
     * @param token   the JWT token of the user making the request
     * @return a message indicating the success of the cancellation
     */
    void cancelTicket(Long eventId, String token) throws ResourceNotFoundException, UnauthorizedAccessException;

    /**
     * Retrieves all booked tickets for a user.
     *
     * @param token the JWT token of the user making the request
     * @return a list of booked tickets
     */
    BookingPageResponseDTO getBookings(String token, Integer pageNo, Integer pageSize, String sortBy) throws ResourceNotFoundException;



}
