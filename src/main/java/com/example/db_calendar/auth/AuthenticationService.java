package com.example.db_calendar.auth;

import com.example.db_calendar.config.JwtService;
import com.example.db_calendar.entity.User;
import com.example.db_calendar.repository.UserRepository;
import com.example.db_calendar.role.Role;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        Optional<User> optionalUser=repository.findByEmail(request.getEmail());
        if (optionalUser.isPresent()) {
            return AuthenticationResponse.builder()
                    .message("registration failed")
                    .status("KO")
                    .build();
        }
        try {
            var user = User.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .number(request.getNumber())
                    .role(Role.USER)
                    .build();
            repository.save(user);
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse
                    .builder()
                    .token(jwtToken)
                    .message(String.valueOf(user.getRole()))
                    .status("OK")
                    .build();
        } catch (Exception e) {
            return AuthenticationResponse.builder()
                    .message("registration failed")
                    .status("KO")
                    .build();
        }

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        System.out.println("request" + request);
        Optional<User> user = repository.findByEmail(request.getEmail());
        if (user.isPresent()) {
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

            } catch (AuthenticationException e) {
                return AuthenticationResponse.builder()
                        .message("User not found")
                        .status("KO")
                        .build();
            }
            var jwtToken = jwtService.generateToken(user.get());
            System.out.println("jwtToken" + jwtToken);
            return AuthenticationResponse
                    .builder()
                    .token(jwtToken)
                    .message(String.valueOf(user.get().getRole()))
                    .status("OK")
                    .build();
        }
        return AuthenticationResponse.builder()
                .message("User not found")
                .status("KO")
                .build();
        }
}
