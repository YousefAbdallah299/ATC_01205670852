package com.yousef.eventbooking.controller;

import com.yousef.eventbooking.dto.request.LoginRequestDTO;
import com.yousef.eventbooking.dto.request.RegisterRequestDTO;
import com.yousef.eventbooking.dto.response.LoginResponseDTO;
import com.yousef.eventbooking.dto.response.RegisterResponseDTO;
import com.yousef.eventbooking.service.security.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    public AuthControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRefresh() {
        ResponseEntity<Void> response = authController.refresh();
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testRegister() {
        RegisterRequestDTO request = new RegisterRequestDTO();
        RegisterResponseDTO responseDTO = new RegisterResponseDTO();
        when(authService.register(request)).thenReturn(responseDTO);

        ResponseEntity<RegisterResponseDTO> response = authController.register(request);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(responseDTO, response.getBody());
        verify(authService).register(request);
    }

    @Test
    void testLogin() {
        LoginRequestDTO request = new LoginRequestDTO();
        LoginResponseDTO responseDTO = new LoginResponseDTO();
        when(authService.login(request)).thenReturn(responseDTO);

        ResponseEntity<LoginResponseDTO> response = authController.login(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseDTO, response.getBody());
        verify(authService).login(request);
    }

    @Test
    void testLogout() {
        String token = "Bearer token";
        ResponseEntity<Void> response = authController.logout(token);

        assertEquals(200, response.getStatusCodeValue());
        verify(authService).logout(token);
    }


}
