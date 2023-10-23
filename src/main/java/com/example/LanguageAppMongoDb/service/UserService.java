package com.example.LanguageAppMongoDb.service;

import com.example.LanguageAppMongoDb.model.users.User;
import com.example.LanguageAppMongoDb.model.verification.VerificationToken;
import com.example.LanguageAppMongoDb.repository.UserRepository;
import com.example.LanguageAppMongoDb.repository.VerificationTokenRepository;
import com.example.LanguageAppMongoDb.resource.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private EmailService emailService;

    public void registerUser(RegistrationRequest registrationRequest) {
        // Validate and process registration request
        // Save user to the database
        User user = new User();
        user.setEmail(registrationRequest.getEmail());

        // Generate verification token
        String verificationToken = generateVerificationToken();

        // Save the verification token in the database
        saveVerificationToken(user.getEmail(), verificationToken);

        // Send the verification email
        emailService.sendVerificationEmail(user.getEmail(), verificationToken);

        // Hash the password before saving
        String hashedPassword = passwordEncoder.encode(registrationRequest.getPassword());
        user.setPassword(hashedPassword);

        user.setVerified(false);

        // Set other user properties as needed
        userRepository.save(user);
    }

//    private void sendVerificationEmail(String email, String verificationToken) {
//// to do with java mail server
//    }


    private String generateVerificationToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] tokenBytes = new byte[30]; // Adjust the length as needed

        secureRandom.nextBytes(tokenBytes);

        // Convert the random bytes to a string
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }

    private void saveVerificationToken(String userEmail, String token) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUserEmail(userEmail);
        verificationToken.setToken(token);
        verificationToken.setExpiryDate(LocalDateTime.now().plusDays(1)); // Token expires in 1 day
        verificationTokenRepository.save(verificationToken);
    }

    public boolean verifyUser(String token) {
        Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByToken(token);

        if (verificationTokenOptional.isPresent()) {
            VerificationToken verificationToken = verificationTokenOptional.get();

            // Check if the token is still valid (not expired)
            if (verificationToken.getExpiryDate().isAfter(LocalDateTime.now())) {
                // Mark the user as verified and update the token status
                User user = userRepository.findByEmail(verificationToken.getUserEmail()).orElse(null);
                if (user != null) {
                    user.setVerified(true);
                    userRepository.save(user);

                    // Mark the token as used (optional, depending on your use case)
                    verificationTokenRepository.delete(verificationToken);

                    return true; // Verification successful
                }
            }
        }

        return false; // Verification failed
    }

}
