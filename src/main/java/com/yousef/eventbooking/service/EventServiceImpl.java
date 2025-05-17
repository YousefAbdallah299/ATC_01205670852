package com.yousef.eventbooking.service;

import com.yousef.eventbooking.dto.enums.EventStatus;
import com.yousef.eventbooking.dto.enums.UserRole;
import com.yousef.eventbooking.dto.request.CreateEventRequestDTO;
import com.yousef.eventbooking.dto.request.UpdateEventRequestDTO;
import com.yousef.eventbooking.dto.response.EventPageResponseDTO;
import com.yousef.eventbooking.dto.response.EventResponseDTO;
import com.yousef.eventbooking.entity.*;
import com.yousef.eventbooking.exception.custom.InvalidInputException;
import com.yousef.eventbooking.exception.custom.ResourceNotFoundException;
import com.yousef.eventbooking.exception.custom.UnauthorizedAccessException;
import com.yousef.eventbooking.repository.*;
import com.yousef.eventbooking.service.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor

public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    private final EventCategoryRepository eventCategoryRepository;

    private final EventTagRepository eventTagRepository;

    private final VenueRepository venueRepository;

    private final JwtUtils jwtUtils;

    private final UserRepository userRepository;

    @Override
    public EventPageResponseDTO getAllEvents(Integer pageNo, Integer pageSize, String sortBy) throws ResourceNotFoundException {


        Sort sort = Sort.by(Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Event> eventPage =  eventRepository.findAll(pageable);

        boolean isLast = pageNo >= eventPage.getTotalPages() - 1;


        if (eventPage.isEmpty()) {
            throw new ResourceNotFoundException("No events found");
        }

        return EventPageResponseDTO.builder()
                .events(eventPage.getContent().stream()
                        .map(Event::toResponseDTO)
                        .toList())
                .pageNumber(eventPage.getNumber())
                .pageSize(eventPage.getSize())
                .totalPages(eventPage.getTotalPages())
                .totalElement(eventPage.getTotalElements())
                .isLast(isLast)
                .build();

    }



    @Override
    @Transactional
    public EventResponseDTO addEvent(CreateEventRequestDTO event, String token) throws InvalidInputException{
        token = token.substring(7);
        String loggedInUserEmail = jwtUtils.getEmailFromJwtToken(token);

        User user =  userRepository.findUserByEmail(loggedInUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole().equals(UserRole.USER)) {
            throw new UnauthorizedAccessException("You are not authorized to access this resource");
        }

        if (event.getEndDate().isBefore(event.getStartDate())) {
            throw new InvalidInputException("End date must be after start date.");
        }

        for (String tagName : event.getTags()) {
            if (Boolean.FALSE.equals(eventTagRepository.existsByName(tagName))) {
                eventTagRepository.save(EventTag.builder()
                        .name(tagName)
                        .build());
            }
        }
        Set<EventTag> tagEntities = new HashSet<>(eventTagRepository.findAllByNameIn(event.getTags()));


        Event.EventBuilder eventBuilder = Event.builder()
                .title(event.getTitle())
                .startDatetime(event.getStartDate())
                .endDatetime(event.getEndDate())
                .category(eventCategoryRepository.findByName(event.getCategoryName()).orElseGet(() ->
                        eventCategoryRepository.save(EventCategory.builder()
                                .name(event.getCategoryName())
                                .build())))
                .venue(venueRepository.findByName(event.getVenueName()).orElseGet(() ->
                        venueRepository.save(Venue.builder()
                                .name(event.getVenueName())
                                .build())))
                .totalTickets(event.getTotalTickets())
                .availableTickets(event.getTotalTickets())
                .ticketPrice(event.getTicketPrice())
                .status(EventStatus.AVAILABLE)
                .tags(tagEntities);

        if (event.getDescription() != null) {
            eventBuilder.description(event.getDescription());
        }

        if (event.getImageUrl() != null) {
            eventBuilder.coverImageUrl(event.getImageUrl());
        }
        Event newEvent = eventBuilder.build();

        eventRepository.save(newEvent);

        return newEvent.toResponseDTO();

    }


    @Transactional
    @Override
    public void deleteEvent(Long eventId, String token) throws ResourceNotFoundException, UnauthorizedAccessException {
        token = token.substring(7);
        String loggedInUserEmail = jwtUtils.getEmailFromJwtToken(token);

        User user = userRepository.findUserByEmail(loggedInUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole().equals(UserRole.USER)) {
            throw new UnauthorizedAccessException("You are not authorized to access this resource");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));


        eventRepository.delete(event);
    }

    @Override
    @Transactional
    public void cancelEvent(Long eventId, String token) throws ResourceNotFoundException, UnauthorizedAccessException {
        token = token.substring(7);
        String loggedInUserEmail = jwtUtils.getEmailFromJwtToken(token);

        User user = userRepository.findUserByEmail(loggedInUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole().equals(UserRole.USER)) {
            throw new UnauthorizedAccessException("You are not authorized to access this resource");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        event.setStatus(EventStatus.CANCELLED);

        eventRepository.save(event);

    }


    @Override
    @Transactional
    public EventResponseDTO updateEvent(Long eventId, UpdateEventRequestDTO updatedEvent, String token) throws ResourceNotFoundException, UnauthorizedAccessException, InvalidInputException {
        token = token.substring(7);
        String loggedInUserEmail = jwtUtils.getEmailFromJwtToken(token);

        User user = userRepository.findUserByEmail(loggedInUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole().equals(UserRole.USER)) {
            throw new UnauthorizedAccessException("You are not authorized to access this resource");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        if (updatedEvent.getStartDate() != null && updatedEvent.getEndDate() != null) {
            if (updatedEvent.getEndDate().isBefore(updatedEvent.getStartDate())) {
                throw new InvalidInputException("End date must be after start date.");
            }
        }

        if (updatedEvent.getTitle() != null) {
            event.setTitle(updatedEvent.getTitle());
        }

        if (updatedEvent.getDescription() != null) {
            event.setDescription(updatedEvent.getDescription());
        }

        if (updatedEvent.getStartDate() != null) {
            event.setStartDatetime(updatedEvent.getStartDate());
        }

        if (updatedEvent.getEndDate() != null) {
            event.setEndDatetime(updatedEvent.getEndDate());
        }

        if (updatedEvent.getTotalTickets() != null) {
            event.setTotalTickets(updatedEvent.getTotalTickets());
        }

        if (updatedEvent.getTicketPrice() != null) {
            event.setTicketPrice(updatedEvent.getTicketPrice());
        }

        if (updatedEvent.getImageUrl() != null) {
            event.setCoverImageUrl(updatedEvent.getImageUrl());
        }
        if (updatedEvent.getCategoryName() != null) {
            event.setCategory(eventCategoryRepository.findByName(updatedEvent.getCategoryName()).orElseGet(() ->
                    eventCategoryRepository.save(EventCategory.builder()
                            .name(updatedEvent.getCategoryName())
                            .build())));
        }
        if (updatedEvent.getVenueName() != null) {
            event.setVenue(venueRepository.findByName(updatedEvent.getVenueName()).orElseGet(() ->
                    venueRepository.save(Venue.builder()
                            .name(updatedEvent.getVenueName())
                            .build())));
        }


        if (updatedEvent.getTags() != null && !updatedEvent.getTags().isEmpty()) {
            for (String tagName : updatedEvent.getTags()) {
                if (Boolean.FALSE.equals(eventTagRepository.existsByName(tagName))) {
                    eventTagRepository.save(EventTag.builder()
                            .name(tagName)
                            .build());
                }
            }
            event.setTags(eventTagRepository.findAllByNameIn(updatedEvent.getTags()));
        }

        eventRepository.save(event);

        return event.toResponseDTO();
    }

    @Override
    public EventResponseDTO findEventById(Long eventId) throws ResourceNotFoundException {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        return event.toResponseDTO();
    }

    @Override
    public EventResponseDTO findEventByTitle(String title) throws ResourceNotFoundException {
        Event event = eventRepository.findEventByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        return event.toResponseDTO();
    }


}
