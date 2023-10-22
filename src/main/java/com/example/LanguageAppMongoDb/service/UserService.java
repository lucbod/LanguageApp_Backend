package com.example.LanguageAppMongoDb.service;

import com.example.LanguageAppMongoDb.model.users.User;
import com.example.LanguageAppMongoDb.repository.UserRepository;
import com.example.LanguageAppMongoDb.resource.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(RegistrationRequest registrationRequest) {
        // Validate and process registration request
        // Save user to the database
        User user = new User();
        user.setEmail(registrationRequest.getEmail());

        // Hash the password before saving
        String hashedPassword = passwordEncoder.encode(registrationRequest.getPassword());
        user.setPassword(hashedPassword);

        // Set other user properties as needed
        userRepository.save(user);
    }
}
