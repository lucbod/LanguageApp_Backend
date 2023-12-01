package com.example.LanguageAppMongoDb.config;

import com.example.LanguageAppMongoDb.model.users.User;
import com.example.LanguageAppMongoDb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository repository;
//    @Bean
//    public UserDetailsService userDetailsService(){
//        return username -> repository.findByEmail(username)
//                .orElseThrow(()->new UsernameNotFoundException("User not found"));
//    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        return username -> {
//            User user = repository.findByEmail(username)
//                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//            // Assuming your User class implements UserDetails
//            return (UserDetails) user;
//        };
//    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        return username -> {
//            System.out.println("Loading user details for username: " + username);
//            User user = repository.findByEmail(username)
//                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//            System.out.println("Loaded user details: " + user);
//            return (UserDetails) user;
//        };
//    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            System.out.println("Loading user details for username: " + username);
            User user = repository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // Extract roles from the 'role' field
            List<GrantedAuthority> authorities = Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
            );

            System.out.println("User roles: " + authorities);

            // Return UserDetails with username, password, and roles
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    authorities
            );
        };
    }


    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

