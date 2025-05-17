package com.yousef.eventbooking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "event_tags")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tag_id;

    @Column(nullable = false, unique = true)
    private String name;
}
