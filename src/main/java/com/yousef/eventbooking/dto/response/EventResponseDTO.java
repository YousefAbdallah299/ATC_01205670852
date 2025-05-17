package com.yousef.eventbooking.dto.response;


import com.yousef.eventbooking.dto.enums.EventStatus;
import com.yousef.eventbooking.entity.EventCategory;
import com.yousef.eventbooking.entity.EventTag;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventResponseDTO {
    private Long eventId;
    private String title;
    private String description;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private String category;
    private Integer totalTickets;
    private Integer availableTickets;
    private Double ticketPrice;
    private EventStatus status;
    private String coverImageUrl;
    private String venue;
    private Set<String> tags;
}
