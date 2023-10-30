package com.example.LanguageAppMongoDb.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if(authHeader ==null ||! authHeader.startsWith("Bearer")){
            System.out.println("No Bearer token found. Skipping authentication.");
            filterChain.doFilter(request,response);
            return;
        }
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUserName(jwt);
        if(userEmail!=null && SecurityContextHolder.getContext().getAuthentication() == null){
            System.out.println("Validating JWT and authenticating user: " + userEmail);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            if(jwtService.isTokenValid(jwt, userDetails)){
                System.out.println("JWT is valid. Creating authentication token.");
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                System.out.println("Authorizing user: " + userEmail);
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("Authorization complete.");

                SecurityContext securityContext = SecurityContextHolder.getContext();
                Authentication authentication = securityContext.getAuthentication();

                if (authentication != null) {
                    System.out.println("Authentication token is set:");
                    System.out.println("Principal: " + authentication.getPrincipal());
                    System.out.println("Authorities: " + authentication.getAuthorities());
                } else {
                    System.out.println("Authentication token is not set.");
                }

            } else {
                System.out.println("JWT validation failed.");
            }
        } else {
            System.out.println("User already authenticated or userEmail is null.");
        }
        filterChain.doFilter(request, response);
    }
}


