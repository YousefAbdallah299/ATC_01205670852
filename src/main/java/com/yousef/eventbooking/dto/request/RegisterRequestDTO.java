package com.yousef.eventbooking.dto.request;


import jakarta.annotation.Nullable;
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
    private String name;

    @Email
    private String email;

    @Size(min = 8)
    private String password;

    @NotBlank
    private String country;

    @Past
    private LocalDate dateOfBirth;

    @Nullable
    private String phoneNumber;



}