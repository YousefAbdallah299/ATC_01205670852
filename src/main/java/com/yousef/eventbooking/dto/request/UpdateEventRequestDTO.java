package com.yousef.eventbooking.dto.request;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateEventRequestDTO {
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer totalTickets;
    private Double ticketPrice;
    private String imageUrl;
    private Set<String> tags;
    private String categoryName;
    private String venueName;

}

