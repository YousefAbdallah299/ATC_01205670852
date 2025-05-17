package com.yousef.eventbooking.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDTO {
    private Long bookingId;

    private String eventName;

    private String venueName;

    private Double price;

    private LocalDateTime bookingDatetime;

    private int numOfTickets;
}
