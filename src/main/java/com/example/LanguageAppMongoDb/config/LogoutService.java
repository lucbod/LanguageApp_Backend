package com.example.LanguageAppMongoDb.config;

import com.example.LanguageAppMongoDb.repository.AccessTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final AccessTokenRepository accessTokenRepository;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if(authHeader ==null ||! authHeader.startsWith("Bearer")){
            System.out.println("Logout: No Bearer token found. Skipping authentication.");
            return;
        }
        jwt = authHeader.substring(7);
        var storedToken = accessTokenRepository.findByToken(jwt)
                .orElse(null);
        if(storedToken !=null){
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            accessTokenRepository.save(storedToken);
        }
    }
}