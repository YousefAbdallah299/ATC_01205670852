package com.yousef.eventbooking.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class RegisterResponseDTO {

    private Long id;

    private String name;

    private String email;

    private LocalDate dateOfBirth;

    private String country;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String phoneNumber;

}