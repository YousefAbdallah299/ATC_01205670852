package com.yousef.eventbooking.service;

import com.yousef.eventbooking.dto.request.CreateEventRequestDTO;
import com.yousef.eventbooking.dto.request.UpdateEventRequestDTO;
import com.yousef.eventbooking.dto.response.EventPageResponseDTO;
import com.yousef.eventbooking.dto.response.EventResponseDTO;
import com.yousef.eventbooking.exception.custom.InvalidInputException;
import com.yousef.eventbooking.exception.custom.ResourceNotFoundException;
import com.yousef.eventbooking.exception.custom.UnauthorizedAccessException;
import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;



public interface EventService {

    /**
     * Retrieves all events from the database.
     *
     * @param pageNo   the page number to retrieve
     * @param pageSize the number of events per page
     * @param sortBy   the field to sort by
     * @return a list of EventResponseDTO objects representing all events
     * @throws ResourceNotFoundException if no events are found
     */
    EventPageResponseDTO getAllEvents(Integer pageNo, Integer pageSize, String sortBy) throws ResourceNotFoundException;


    /**
     * Adds a new event to the database.
     *
     * @param event the event to add
     * @param token the JWT token of the user making the request
     * @return an EventResponseDTO object representing the added event
     * @throws UnauthorizedAccessException if the user is not authorized to add events
     */
    EventResponseDTO addEvent(@Valid CreateEventRequestDTO event, String token);

    @Transactional
    /**
     * Updates an existing event in the database.
     *
     * @param eventId the ID of the event to delete
     * @param token the JWT token of the user making the request
     * @throws ResourceNotFoundException if the event with the specified ID is not found
     * @throws UnauthorizedAccessException if the user is not authorized to delete the event
     */
    void deleteEvent(Long eventId, String token) throws ResourceNotFoundException, UnauthorizedAccessException;


    /**
     * Cancels an event in the database.
     *
     * @param eventId the ID of the event to cancel
     * @param token   the JWT token of the user making the request
     * @throws ResourceNotFoundException   if the event with the specified ID is not found
     * @throws UnauthorizedAccessException if the user is not authorized to cancel the event
     */
     void cancelEvent(Long eventId, String token) throws ResourceNotFoundException, UnauthorizedAccessException;


    /**
     * Updates an existing event in the database.
     *
     * @param eventId      the ID of the event to update
     * @param updatedEvent the updated event data
     * @param token        the JWT token of the user making the request
     * @return an EventResponseDTO object representing the updated event
     * @throws ResourceNotFoundException   if the event with the specified ID is not found
     * @throws UnauthorizedAccessException if the user is not authorized to update the event
     */
     EventResponseDTO updateEvent(Long eventId, UpdateEventRequestDTO updatedEvent, String token) throws ResourceNotFoundException, UnauthorizedAccessException, InvalidInputException;


    /**
     * Retrieves an event by its ID.
     *
     * @param eventId the ID of the event to retrieve
     * @return an EventResponseDTO object representing the event with the specified ID
     * @throws ResourceNotFoundException if the event with the specified ID is not found
     */
     EventResponseDTO findEventById(Long eventId) throws ResourceNotFoundException;


    /**
     * Retrieves an event by its title.
     *
     * @param title the title of the event to retrieve
     * @return an EventResponseDTO object representing the event with the specified title
     * @throws ResourceNotFoundException if the event with the specified title is not found
     */
    EventResponseDTO findEventByTitle(String title) throws ResourceNotFoundException;
}