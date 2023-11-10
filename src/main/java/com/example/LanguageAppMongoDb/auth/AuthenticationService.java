package com.example.LanguageAppMongoDb.auth;

import ch.qos.logback.classic.encoder.JsonEncoder;
import com.example.LanguageAppMongoDb.config.JwtService;
import com.example.LanguageAppMongoDb.model.users.Role;
import com.example.LanguageAppMongoDb.model.users.User;
import com.example.LanguageAppMongoDb.model.verification.AccessToken;
import com.example.LanguageAppMongoDb.repository.UserRepository;
import com.example.LanguageAppMongoDb.service.AccessTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private static final long EXPIRATION_TIME = 15 * 60 * 1000; // 15 minutes in milliseconds

    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;



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

            // setting expiration time
            Date expiryDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME);

            // Save the access token to the AccessToken collection
            AccessToken accessToken = new AccessToken();
            accessToken.setToken(jwtToken);
            accessToken.setExpiryDate(expiryDate); // set your expiry date here;
            accessToken.setUserEmail(user.getEmail());

            // hashing token
            String hashedToken = bCryptPasswordEncoder.encode(accessToken.getToken());
            accessToken.setToken(hashedToken);

            accessTokenService.saveAccessToken(accessToken);

            return AuthenticationResponse.builder()
                    .token(jwtToken).build();
        } catch (AuthenticationException e) {
            System.out.println("Authentication failed for email: " + request.getEmail());
            e.printStackTrace(); // Add this line to print the stack trace
            throw e; // Rethrow the exception to propagate it
        }
    }

}
