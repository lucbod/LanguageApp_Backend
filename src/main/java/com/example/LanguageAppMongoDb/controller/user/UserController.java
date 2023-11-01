package com.example.LanguageAppMongoDb.controller.user;

import com.example.LanguageAppMongoDb.model.users.User;
import com.example.LanguageAppMongoDb.resource.RegistrationRequest;
import com.example.LanguageAppMongoDb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")  // Base path for all endpoints in this controller
public class UserController {

    @Autowired
    private UserService userService;

    // old endpoint
//    @PostMapping("/register")
//    public ResponseEntity<String> registerUser(@RequestBody RegistrationRequest registrationRequest) {
//        // Validate and process registration request
//        userService.registerUser(registrationRequest);
//        return ResponseEntity.ok("User registered successfully");
//    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
