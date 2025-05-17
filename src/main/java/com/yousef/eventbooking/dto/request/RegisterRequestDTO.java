package com.yousef.eventbooking.dto.request;


import com.yousef.eventbooking.dto.enums.UserRole;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class RegisterRequestDTO {

    @NotBlank
    private String first_name;

    @NotBlank
    private String last_name;

    @Email
    private String email;

    @Size(min = 8)
    private String password;

    @Nullable
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private UserRole role;



}