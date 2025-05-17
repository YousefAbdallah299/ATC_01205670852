package com.yousef.eventbooking.service;

import com.yousef.eventbooking.dto.enums.EventStatus;
import com.yousef.eventbooking.dto.enums.UserRole;
import com.yousef.eventbooking.dto.response.BookingPageResponseDTO;
import com.yousef.eventbooking.dto.response.BookingResponseDTO;
import com.yousef.eventbooking.entity.Booking;
import com.yousef.eventbooking.entity.Event;
import com.yousef.eventbooking.entity.User;
import com.yousef.eventbooking.exception.custom.EventAlreadyBookedException;
import com.yousef.eventbooking.exception.custom.InsufficientTicketsException;
import com.yousef.eventbooking.exception.custom.ResourceNotFoundException;
import com.yousef.eventbooking.exception.custom.UnauthorizedAccessException;
import com.yousef.eventbooking.repository.EventRepository;
import com.yousef.eventbooking.repository.UserRepository;
import com.yousef.eventbooking.service.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.yousef.eventbooking.repository.BookingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final JwtUtils jwtUtils;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    @Transactional
    @Override
    public BookingResponseDTO bookTicket(Long eventId, String token) throws ResourceNotFoundException, InsufficientTicketsException{
        token = token.substring(7);
        String loggedInUserEmail = jwtUtils.getEmailFromJwtToken(token);

        User user = userRepository.findUserByEmail(loggedInUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        if (event.getAvailableTickets() <= 0) {
            throw new InsufficientTicketsException("No available tickets for this event");
        }


        event.setAvailableTickets(event.getAvailableTickets() - 1);
        if(event.getAvailableTickets() == 0) {
            event.setStatus(EventStatus.SOLD_OUT);
        }
        eventRepository.save(event);

        if(bookingRepository.existsByEvent_idAndUser_Email(eventId, loggedInUserEmail)){
            Booking booking = bookingRepository.findByEvent_idAndUser_Email(eventId, loggedInUserEmail)
                    .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
            booking.setNum_of_tickets(booking.getNum_of_tickets() + 1);
            bookingRepository.save(booking);
            return booking.toResponseDTO();


        }
        else {
            Booking booking = Booking.builder()
                    .user(user)
                    .event(event)
                    .price(event.getTicketPrice())
                    .bookingDatetime(LocalDateTime.now())
                    .num_of_tickets(1)
                    .build();
            bookingRepository.save(booking);
            return booking.toResponseDTO();


        }




    }

    @Transactional
    @Override
    public void cancelTicket(Long bookingID, String token) throws ResourceNotFoundException, UnauthorizedAccessException {
        token = token.substring(7);
        String loggedInUserEmail = jwtUtils.getEmailFromJwtToken(token);

        User user = userRepository.findUserByEmail(loggedInUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Booking booking = bookingRepository.findById(bookingID)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        Event event = eventRepository.findById(booking.getEvent().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        if (event.getStartDatetime().minusHours(24).isBefore(LocalDateTime.now())) {
            throw new UnauthorizedAccessException("Cannot cancel booking within 24 hours of event.");
        }
        if (!booking.getUser().getEmail().equals(loggedInUserEmail)) {
            throw new UnauthorizedAccessException("You are not authorized to cancel this booking.");
        }


        event.setAvailableTickets(event.getAvailableTickets() + 1);
        if(event.getStatus() == EventStatus.SOLD_OUT) {
            event.setStatus(EventStatus.AVAILABLE);
        }
        eventRepository.save(event);


        if (booking.getNum_of_tickets() > 1) {
            booking.setNum_of_tickets(booking.getNum_of_tickets() - 1);
            bookingRepository.save(booking);
        }
        else {
            bookingRepository.delete(booking);
        }


    }

    @Override
    public BookingPageResponseDTO getBookings(String token,Integer pageNo, Integer pageSize, String sortBy) throws ResourceNotFoundException {
        token = token.substring(7);
        String loggedInUserEmail = jwtUtils.getEmailFromJwtToken(token);

        User user = userRepository.findUserByEmail(loggedInUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));


        Sort sort = Sort.by(Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Booking> bookingPage =  bookingRepository.findAllByUser_Email(loggedInUserEmail, pageable);


        boolean isLast = pageNo >= bookingPage.getTotalPages() - 1;

        return BookingPageResponseDTO.builder()
                .bookings(bookingPage.getContent().stream()
                        .map(Booking::toResponseDTO)
                        .toList())
                .totalPages(bookingPage.getTotalPages())
                .pageSize(bookingPage.getSize())
                .pageNumber(bookingPage.getNumber())
                .totalElement(bookingPage.getTotalElements())
                .isLast(isLast)
                .build();

    }


}
