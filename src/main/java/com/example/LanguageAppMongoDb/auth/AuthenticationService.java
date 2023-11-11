package com.example.LanguageAppMongoDb.auth;

import ch.qos.logback.classic.encoder.JsonEncoder;
import com.example.LanguageAppMongoDb.config.JwtService;
import com.example.LanguageAppMongoDb.model.users.Role;
import com.example.LanguageAppMongoDb.model.users.User;
import com.example.LanguageAppMongoDb.model.verification.AccessToken;
import com.example.LanguageAppMongoDb.model.verification.TokenType;
import com.example.LanguageAppMongoDb.repository.AccessTokenRepository;
import com.example.LanguageAppMongoDb.repository.UserRepository;
import com.example.LanguageAppMongoDb.service.AccessTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;

    private final AccessTokenRepository accessTokenRepository;

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
        var refreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws CustomAuthenticationException {
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
            var refreshToken = jwtService.generateRefreshToken(user);

            // setting expiration time
            Date expiryDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME);

            // first revoke then save
            revokeAllUserTokens(user);
            System.out.println("auth revoked succ");

            // Save the access token to the AccessToken collection
            AccessToken accessToken = new AccessToken();
            accessToken.setToken(jwtToken);
            accessToken.setExpiryDate(expiryDate); // set your expiry date here;
            accessToken.setUserEmail(user.getEmail());
            accessToken.setRevoked(false);
            accessToken.setExpired(false);
            accessToken.setTokenType(TokenType.ACCESS_TOKEN);

//            // hashing token
//            String hashedToken = bCryptPasswordEncoder.encode(accessToken.getToken());
//            accessToken.setToken(hashedToken);


            try{
                accessTokenService.saveAccessToken(accessToken);
                System.out.println("Token updated: " + accessToken.getToken());
            } catch (Exception e){
                System.out.println("Error saving token "+ e.getMessage());
                e.printStackTrace();
            }

            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .build();

        } catch (AuthenticationException e) {
            System.out.println("Authentication failed for email: " + request.getEmail());
            e.printStackTrace(); // Add this line to print the stack trace
//            throw e; // Rethrow the exception to propagate it
            throw new CustomAuthenticationException("Invalid credentials: ", e);
        }
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if(authHeader ==null ||! authHeader.startsWith("Bearer")){
            System.out.println("No Bearer token found. Skipping authentication.");
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUserName(refreshToken);
        if(userEmail!=null){
            System.out.println("Validating JWT and authenticating user: " + userEmail);
            var user = this.repository.findByEmail(userEmail)
                    .orElseThrow();
            if(jwtService.isTokenValid(refreshToken, user)) {
                System.out.println("RefreshToken is valid.");
                var accessToken = jwtService.generateToken(user);

                revokeAllUserTokens(user);

                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);

                System.out.println("User tokens updated successfully.");

            }}
    }

    private void revokeAllUserTokens(User user) {
        List<AccessToken> validUserTokens = accessTokenRepository.findAllValidTokenByUserEmail(user.getEmail());

        if (validUserTokens.isEmpty())
            return;

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        try {
            // Save all tokens using saveAll
            List<AccessToken> savedTokens = accessTokenRepository.saveAll(validUserTokens);

            // Log the saved tokens to check if they have been updated
            savedTokens.forEach(savedToken -> {
                System.out.println("Saved token - Revoked: " + savedToken.isRevoked() + ", Expired: " + savedToken.isExpired());
            });
        } catch (Exception e) {
            System.out.println("Error by expiration and revoke " + e.getMessage());
        }
    }
}
