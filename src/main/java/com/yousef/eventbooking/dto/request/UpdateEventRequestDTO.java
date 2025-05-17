package com.yousef.eventbooking.dto.request;


import com.yousef.eventbooking.dto.enums.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

