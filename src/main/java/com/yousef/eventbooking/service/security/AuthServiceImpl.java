package com.yousef.eventbooking.service.security;
import com.yousef.eventbooking.dto.request.ChangePasswordDTO;
import com.yousef.eventbooking.dto.request.LoginRequestDTO;
import com.yousef.eventbooking.dto.request.RegisterRequestDTO;
import com.yousef.eventbooking.dto.response.RegisterResponseDTO;
import com.yousef.eventbooking.dto.response.LoginResponseDTO;
import com.yousef.eventbooking.entity.Customer;
import com.yousef.eventbooking.exception.custom.EmailAlreadyExistsException;
import com.yousef.eventbooking.exception.custom.InvalidOldPasswordException;
import com.yousef.eventbooking.exception.custom.ResourceNotFoundException;
import com.yousef.eventbooking.exception.custom.SameAsOldPasswordException;
import com.yousef.eventbooking.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final CustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;

    private final AuthenticationManager authenticationManager;

    private final Set<String> invalidatedTokens = new HashSet<>();

    @Transactional
    public RegisterResponseDTO register(RegisterRequestDTO customerRequest) throws EmailAlreadyExistsException{

        if(Boolean.TRUE.equals(customerRepository.existsByEmail(customerRequest.getEmail())))
            throw new EmailAlreadyExistsException("Email Already Exists!");

        Customer customer = Customer.builder()
                .email(customerRequest.getEmail())
                .password(this.passwordEncoder.encode(customerRequest.getPassword()))
                .name(customerRequest.getName())
                .dateOfBirth(customerRequest.getDateOfBirth())
                .country(customerRequest.getCountry())
                .build();

        if(customerRequest.getPhoneNumber() != null) customer.setPhoneNumber(customerRequest.getPhoneNumber());

//        Account account = Account.builder()
//                .balance(0.0)
//                .accountType(AccountType.SAVINGS)
//                .accountDescription("Savings Account")
//                .accountName(customerRequest.getName())
//                .currency(AccountCurrency.EGP)
//                .accountNumber(new SecureRandom().nextInt(1000000000) + "")
//                .customer(customer)
//                .build();
//
//        customer.getAccounts().add(account);

        Customer savedCustomer = customerRepository.save(customer);

        return savedCustomer.toResponse();
    }


    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        return LoginResponseDTO.builder()
                .token(jwt)
                .message("Login Success!")
                .httpStatus(HttpStatus.ACCEPTED)
                .tokenType("Bearer")
                .build();

    }

    @Override
    public void logout(String token){
        invalidatedTokens.add(token);
    }

    @Override
    public void changePassword(ChangePasswordDTO changePasswordDTO, String loggedInUserEmail) throws ResourceNotFoundException, SameAsOldPasswordException, InvalidOldPasswordException {
        Customer customer =  customerRepository.findUserByEmail(loggedInUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (passwordEncoder.matches(changePasswordDTO.getNewPassword(), customer.getPassword())) {
            throw new SameAsOldPasswordException("New password is the same as the old one!");
        }

        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), customer.getPassword())) {
            throw new InvalidOldPasswordException("Old password is incorrect!");
        }

        String newPass = passwordEncoder.encode(changePasswordDTO.getNewPassword());
        customer.setPassword(newPass);


        customerRepository.save(customer);


    }

    public boolean isTokenInvalid(String token){
        return invalidatedTokens.contains(token);
    }

}