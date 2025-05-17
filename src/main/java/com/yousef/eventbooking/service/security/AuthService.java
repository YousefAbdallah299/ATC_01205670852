package com.yousef.eventbooking.service.security;

import com.yousef.eventbooking.dto.request.LoginRequestDTO;
import com.yousef.eventbooking.dto.request.RegisterRequestDTO;
import com.yousef.eventbooking.dto.response.LoginResponseDTO;
import com.yousef.eventbooking.dto.response.RegisterResponseDTO;
import com.yousef.eventbooking.exception.custom.EmailAlreadyExistsException;


public interface AuthService {

    /**
     * Register a new customer
     *
     * @param customer the customer to be registered
     * @return the registered customer
     * @throws EmailAlreadyExistsException if the customer already exists
     */

    RegisterResponseDTO register(RegisterRequestDTO customer) throws EmailAlreadyExistsException;


    /**
     * Login a customer
     *
     * @param loginRequestDTO login details
     * @return login response @{@link LoginResponseDTO}
     */

    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);


    /**
     * Logout a customer
     *
     * @param token the token of the user
     */
    void logout(String token);


}