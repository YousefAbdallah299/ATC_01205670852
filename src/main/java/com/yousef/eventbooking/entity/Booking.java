package com.yousef.eventbooking.entity;

import com.yousef.eventbooking.dto.response.BookingResponseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "bookings",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "event_id", "booking_datetime"})
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_booking_user", foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE")
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "event_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_booking_event", foreignKeyDefinition = "FOREIGN KEY (event_id) REFERENCES events(event_id) ON DELETE CASCADE")
    )
    private Event event;


    @Column(nullable = false, name = "booking_datetime")
    private LocalDateTime bookingDatetime;

    @Column(nullable = false)
    private Double price;


    @Column(nullable = false)
    private int num_of_tickets;

    public BookingResponseDTO toResponseDTO() {
        return BookingResponseDTO.builder()
                .bookingId(bookingId)
                .eventName(event.getTitle())
                .venueName(event.getVenue().getName())
                .price(price)
                .bookingDatetime(bookingDatetime)
                .numOfTickets(num_of_tickets)
                .build();
    }

}
