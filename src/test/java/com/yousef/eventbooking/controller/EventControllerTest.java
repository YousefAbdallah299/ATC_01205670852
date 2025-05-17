package com.yousef.eventbooking.controller;

import com.yousef.eventbooking.dto.request.CreateEventRequestDTO;
import com.yousef.eventbooking.dto.request.UpdateEventRequestDTO;
import com.yousef.eventbooking.dto.response.EventPageResponseDTO;
import com.yousef.eventbooking.dto.response.EventResponseDTO;
import com.yousef.eventbooking.service.EventService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    public EventControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEvents() {
        EventPageResponseDTO responseDTO = new EventPageResponseDTO(
                null,
                0,
                5,
                1L,
                1,
                false
        );

        when(eventService.getAllEvents(0, 5, "id")).thenReturn(responseDTO);

        ResponseEntity<EventPageResponseDTO> response = eventController.getAllEvents(0, 5, "id");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseDTO, response.getBody());
        verify(eventService).getAllEvents(0, 5, "id");
    }

    @Test
    void testFindEventById() {
        Long eventId = 1L;
        EventResponseDTO responseDTO = new EventResponseDTO();
        when(eventService.findEventById(eventId)).thenReturn(responseDTO);

        ResponseEntity<EventResponseDTO> response = eventController.findEventById(eventId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseDTO, response.getBody());
        verify(eventService).findEventById(eventId);
    }

    @Test
    void testFindEventByTitle() {
        String title = "Test Event";
        EventResponseDTO responseDTO = new EventResponseDTO();
        when(eventService.findEventByTitle(title)).thenReturn(responseDTO);

        ResponseEntity<EventResponseDTO> response = eventController.findEventByTitle(title);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseDTO, response.getBody());
        verify(eventService).findEventByTitle(title);
    }


    @Test
    void testAddEvent() {
        CreateEventRequestDTO request = new CreateEventRequestDTO();
        String token = "Bearer token";
        EventResponseDTO responseDTO = new EventResponseDTO();
        when(eventService.addEvent(request, token)).thenReturn(responseDTO);

        ResponseEntity<EventResponseDTO> response = eventController.addEvent(request, token);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(responseDTO, response.getBody());
        verify(eventService).addEvent(request, token);
    }

    @Test
    void testUpdateEvent() {
        Long eventId = 1L;
        UpdateEventRequestDTO request = new UpdateEventRequestDTO();
        String token = "Bearer token";
        EventResponseDTO responseDTO = new EventResponseDTO();
        when(eventService.updateEvent(eventId, request, token)).thenReturn(responseDTO);

        ResponseEntity<EventResponseDTO> response = eventController.updateEvent(eventId, request, token);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseDTO, response.getBody());
        verify(eventService).updateEvent(eventId, request, token);
    }

    @Test
    void testDeleteEvent() {
        Long eventId = 1L;
        String token = "Bearer token";

        ResponseEntity<Void> response = eventController.deleteEvent(eventId, token);

        assertEquals(204, response.getStatusCodeValue());
        verify(eventService).deleteEvent(eventId, token);
    }

    @Test
    void testCancelEvent() {
        Long eventId = 1L;
        String token = "Bearer token";

        ResponseEntity<Void> response = eventController.cancelEvent(eventId, token);

        assertEquals(204, response.getStatusCodeValue());
        verify(eventService).cancelEvent(eventId, token);
    }
}
