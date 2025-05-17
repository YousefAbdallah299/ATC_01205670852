package com.yousef.eventbooking.service.security;

import com.yousef.eventbooking.dto.request.ChangePasswordDTO;
import com.yousef.eventbooking.dto.request.LoginRequestDTO;
import com.yousef.eventbooking.dto.request.RegisterRequestDTO;
import com.yousef.eventbooking.dto.response.LoginResponseDTO;
import com.yousef.eventbooking.dto.response.RegisterResponseDTO;
import com.yousef.eventbooking.exception.custom.EmailAlreadyExistsException;
import com.yousef.eventbooking.exception.custom.InvalidOldPasswordException;
import com.yousef.eventbooking.exception.custom.ResourceNotFoundException;
import com.yousef.eventbooking.exception.custom.SameAsOldPasswordException;


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


    /**
     * Logout a customer
     *
     * @param changePasswordDTO the new password of the user
     */
    void changePassword(ChangePasswordDTO changePasswordDTO, String token) throws ResourceNotFoundException, SameAsOldPasswordException, InvalidOldPasswordException;


}