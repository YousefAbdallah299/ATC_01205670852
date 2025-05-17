package com.yousef.eventbooking.service.security;
import com.yousef.eventbooking.dto.enums.UserRole;
import com.yousef.eventbooking.dto.request.LoginRequestDTO;
import com.yousef.eventbooking.dto.request.RegisterRequestDTO;
import com.yousef.eventbooking.dto.response.RegisterResponseDTO;
import com.yousef.eventbooking.dto.response.LoginResponseDTO;
import com.yousef.eventbooking.entity.User;
import com.yousef.eventbooking.exception.custom.EmailAlreadyExistsException;
import com.yousef.eventbooking.repository.UserRepository;
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

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;

    private final AuthenticationManager authenticationManager;

    private final Set<String> invalidatedTokens = new HashSet<>();

    @Transactional
    public RegisterResponseDTO register(RegisterRequestDTO customerRequest) throws EmailAlreadyExistsException{

        if(Boolean.TRUE.equals(userRepository.existsByEmail(customerRequest.getEmail())))
            throw new EmailAlreadyExistsException("Email Already Exists!");

        User user = User.builder()
                .email(customerRequest.getEmail())
                .passwordHash(this.passwordEncoder.encode(customerRequest.getPassword()))
                .firstName(customerRequest.getFirst_name())
                .lastName(customerRequest.getLast_name())
                .role(customerRequest.getRole() != null ? customerRequest.getRole() : UserRole.USER)
                .build();

        if(customerRequest.getPhoneNumber() != null) user.setPhoneNumber(customerRequest.getPhoneNumber());
        if(customerRequest.getRole() != null) user.setPhoneNumber(customerRequest.getPhoneNumber());


        User savedUser = userRepository.save(user);

        return savedUser.toRegisterResponseDTO();
    }


    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        jwtUtils.getEmailFromJwtToken(jwt);
        User user = userRepository.findUserByEmail(jwtUtils.getEmailFromJwtToken(jwt))
                .orElseThrow(() -> new RuntimeException("User Not Found with email: " + jwtUtils.getEmailFromJwtToken(jwt)));


        return LoginResponseDTO.builder()
                .token(jwt)
                .message("Login Success!")
                .httpStatus(HttpStatus.ACCEPTED)
                .tokenType("Bearer")
                .role(user.getRole())
                .build();

    }

    @Override
    public void logout(String token){
        token = token.substring(7);
        invalidatedTokens.add(token);
    }


    public boolean isTokenInvalid(String token){
        return invalidatedTokens.contains(token);
    }

}