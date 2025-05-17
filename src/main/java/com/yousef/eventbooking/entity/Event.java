package com.yousef.eventbooking.entity;

import com.yousef.eventbooking.dto.enums.EventStatus;
import com.yousef.eventbooking.dto.response.EventResponseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "events")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, name = "start_datetime")
    private LocalDateTime startDatetime;


    @Column(nullable = false, name = "end_datetime")
    private LocalDateTime endDatetime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "category_id",
            foreignKey = @ForeignKey(name = "fk_event_category",
                    foreignKeyDefinition = "FOREIGN KEY (category_id) REFERENCES event_categories(category_id) ON DELETE SET NULL")
    )
    private EventCategory category;


    @Column(nullable = false, name = "total_tickets")
    private Integer totalTickets;

    @Column(nullable = false, name = "available_tickets")
    private Integer availableTickets;

    @Column(nullable = false, name = "ticket_price")
    private Double ticketPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus status = EventStatus.AVAILABLE;

    @Column(name = "cover_image_url", columnDefinition = "TEXT")
    private String coverImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "venue",
            foreignKey = @ForeignKey(name = "fk_venue",
                    foreignKeyDefinition = "FOREIGN KEY (venue) REFERENCES venues(id) ON DELETE SET NULL")
    )
    private Venue venue;



    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "event_event_tags",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<EventTag> tags = new HashSet<>();


    public EventResponseDTO toResponseDTO() {
        return EventResponseDTO.builder()
                .eventId(this.id)
                .title(this.title)
                .description(this.description)
                .startDatetime(this.startDatetime)
                .endDatetime(this.endDatetime)
                .category(this.category.getName())
                .totalTickets(this.totalTickets)
                .availableTickets(this.availableTickets)
                .ticketPrice(this.ticketPrice)
                .venue(this.venue.getName())
                .status(this.status)
                .coverImageUrl(this.coverImageUrl)
                .tags(this.tags != null ? this.tags.stream().map(EventTag::getName).collect(Collectors.toSet()) : null)
                .build();
    }

}
