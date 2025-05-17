package com.yousef.eventbooking.entity;

import com.yousef.eventbooking.dto.enums.UserRole;
import com.yousef.eventbooking.dto.response.RegisterResponseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @Column(nullable = false, name = "first_name")
    private String firstName;

    @Column(nullable = false, name = "last_name")
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, name = "password_hash")
    private String passwordHash;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "user_role")
    private UserRole role;


    public RegisterResponseDTO toRegisterResponseDTO() {
        return RegisterResponseDTO.builder()
                .id(this.user_id)
                .name(this.firstName + " " + this.lastName)
                .email(this.email)
                .phoneNumber(this.phoneNumber)
                .build();
    }
}


