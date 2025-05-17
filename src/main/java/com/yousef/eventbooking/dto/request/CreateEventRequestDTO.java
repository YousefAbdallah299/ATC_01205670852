package com.yousef.eventbooking.dto.request;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateEventRequestDTO {

    @NotBlank(message = "Event title cannot be blank")
    private String title;

    private String description;

    @NotNull(message = "Start date cannot be empty")
    @DateTimeFormat()
    private LocalDateTime startDate;

    @NotNull(message = "End date cannot be empty")
    @DateTimeFormat()
    private LocalDateTime endDate;

    @NotNull(message = "Category cannot be empty")
    private String categoryName;

    @NotNull(message = "Total tickets cannot be empty")
    @Min(value = 1, message = "Total tickets must be at least 1")
    private Integer totalTickets;

    @NotNull(message = "Ticket price is empty")
    @DecimalMin(value = "0.0", message = "Ticket price cannot be negative")
    private Double ticketPrice;

    @NotNull(message = "Event tags cannot be empty")
    private Set<String> tags;

    private String imageUrl;

    @NotNull(message = "Venue name cannot be empty")
    private String venueName;

}