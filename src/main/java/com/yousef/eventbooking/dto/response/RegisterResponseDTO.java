package com.yousef.eventbooking.dto.response;

import lombok.*;
import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class RegisterResponseDTO {

    private Long id;

    private String name;

    private String email;

    private String phoneNumber;


}