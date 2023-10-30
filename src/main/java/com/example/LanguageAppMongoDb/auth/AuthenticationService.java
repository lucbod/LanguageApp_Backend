package com.example.LanguageAppMongoDb.auth;

import com.example.LanguageAppMongoDb.config.JwtService;
import com.example.LanguageAppMongoDb.model.users.Role;
import com.example.LanguageAppMongoDb.model.users.User;
import com.example.LanguageAppMongoDb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            System.out.println("Attempting authentication for email: " + request.getEmail());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            System.out.println("Authentication successful for email: " + request.getEmail());


            // Log the email before querying the database
            System.out.println("Email to authenticate: " + request.getEmail());

            var user = repository.findByEmail(request.getEmail())
                    .orElseThrow();
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken).build();
        } catch (AuthenticationException e) {
            System.out.println("Authentication failed for email: " + request.getEmail());
            e.printStackTrace(); // Add this line to print the stack trace
            throw e; // Rethrow the exception to propagate it
        }
    }

}
