package com.yousef.eventbooking.controller;


import com.yousef.eventbooking.dto.request.CreateEventRequestDTO;
import com.yousef.eventbooking.dto.request.UpdateEventRequestDTO;
import com.yousef.eventbooking.dto.response.EventPageResponseDTO;
import com.yousef.eventbooking.dto.response.EventResponseDTO;
import com.yousef.eventbooking.exception.custom.InvalidInputException;
import com.yousef.eventbooking.exception.custom.ResourceNotFoundException;
import com.yousef.eventbooking.exception.custom.UnauthorizedAccessException;
import com.yousef.eventbooking.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/event")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
@Tag(name = "Events Management", description = "Endpoints for the admins to manage events.")

public class EventController {

    private final EventService eventService;



    @GetMapping("/all")
    @Operation(summary = "Get all events", description = "Retrieves the list of events found.")
    public ResponseEntity<EventPageResponseDTO> getAllEvents(
                                                             @RequestParam(defaultValue = "0") Integer pageNo,
                                                             @RequestParam(defaultValue = "5") Integer pageSize,
                                                             @RequestParam(defaultValue = "id") String sortBy) throws ResourceNotFoundException {
        EventPageResponseDTO events = eventService.getAllEvents(pageNo, pageSize, sortBy);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    @Operation(summary = "Get event by ID", description = "Retrieves the details of a specific event.")
    public ResponseEntity<EventResponseDTO> findEventById(@PathVariable Long eventId) throws ResourceNotFoundException {
        EventResponseDTO event = eventService.findEventById(eventId);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }


    @GetMapping("/name/{title}")
    @Operation(summary = "Get event by title", description = "Retrieves the details of a specific event.")
    public ResponseEntity<EventResponseDTO> findEventByTitle(@PathVariable String title) throws ResourceNotFoundException {
        EventResponseDTO event = eventService.findEventByTitle(title);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @PostMapping("/add")
    @Operation(summary = "Add a new event", description = "Adds a new event to the system.")
    public ResponseEntity<EventResponseDTO> addEvent(@RequestBody @Valid CreateEventRequestDTO event, @RequestHeader("Authorization") String token) {
        EventResponseDTO createdEvent = eventService.addEvent(event, token);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @PutMapping("/update/{eventId}")
    @Operation(summary = "Update an existing event", description = "Updates the details of an existing event.")
    public ResponseEntity<EventResponseDTO> updateEvent(@PathVariable Long eventId, @RequestBody @Valid UpdateEventRequestDTO event, @RequestHeader("Authorization") String token) throws ResourceNotFoundException, UnauthorizedAccessException, InvalidInputException {
        EventResponseDTO updatedEvent = eventService.updateEvent(eventId, event, token);
        return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{eventId}")
    @Operation(summary = "Delete an event", description = "Deletes an event from the system.")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId, @RequestHeader("Authorization") String token) throws ResourceNotFoundException, UnauthorizedAccessException {
        eventService.deleteEvent(eventId, token);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/cancel/{eventId}")
    @Operation(summary = "Cancel an event", description = "Cancels an event in the system.")
    public ResponseEntity<Void> cancelEvent(@PathVariable Long eventId, @RequestHeader("Authorization") String token) throws ResourceNotFoundException, UnauthorizedAccessException {
        eventService.cancelEvent(eventId, token);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }




}
