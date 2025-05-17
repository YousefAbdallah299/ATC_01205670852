package com.yousef.eventbooking.controller;

import com.yousef.eventbooking.dto.request.ChangePasswordDTO;
import com.yousef.eventbooking.dto.request.LoginRequestDTO;
import com.yousef.eventbooking.dto.request.RegisterRequestDTO;
import com.yousef.eventbooking.dto.response.LoginResponseDTO;
import com.yousef.eventbooking.dto.response.RegisterResponseDTO;
import com.yousef.eventbooking.exception.custom.EmailAlreadyExistsException;
import com.yousef.eventbooking.exception.custom.InvalidOldPasswordException;
import com.yousef.eventbooking.exception.custom.ResourceNotFoundException;
import com.yousef.eventbooking.exception.custom.SameAsOldPasswordException;
import com.yousef.eventbooking.service.security.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
@Tag(name = "Authentication", description = "Endpoints for customer authentication and authorization, including registration, login, and logout.")
public class AuthController {

    private final AuthService authService;

    //THIS IS ONLY FOR KEEPING THE SESSION ALIVE IN RENDER, PLEASE IGNORE IT
    @GetMapping("/refresh")
    public ResponseEntity<Void> refresh() {
        return ResponseEntity.ok().build();
    }


    @PostMapping("/register")
    @Operation(summary = "Register a new customer", description = "Creates a new customer account with the provided details. An email address must be unique.")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody @Valid RegisterRequestDTO customer) throws EmailAlreadyExistsException {
        return new ResponseEntity<>(this.authService.register(customer), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "Login with email and password", description = "Authenticates a customer by email and password, and returns a JWT token upon successful login.")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        return new ResponseEntity<>(this.authService.login(loginRequestDTO), HttpStatus.OK);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout the user", description = "Invalidates the JWT token for the currently logged-in customer, logging them out of the system.")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return ResponseEntity.ok().build();
    }

    @PutMapping("change-password")
    @Operation(summary = "Change user password", description = "Changes the current user password to a new one.")

    public ResponseEntity<Void> changePassword(@RequestHeader("Authorization") String token, @RequestBody @Valid ChangePasswordDTO changePasswordDTO) throws SameAsOldPasswordException, ResourceNotFoundException, InvalidOldPasswordException {

        authService.changePassword(changePasswordDTO, token);

        return ResponseEntity.ok().build();
    }


}