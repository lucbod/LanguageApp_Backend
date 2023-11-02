package com.example.LanguageAppMongoDb.controller.profile;

import com.example.LanguageAppMongoDb.resource.ProfileRequest;
import com.example.LanguageAppMongoDb.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PostMapping("/create")
    public ResponseEntity<String> createProfile(
            @RequestHeader("Authorization") String token,
            @RequestBody ProfileRequest profileRequest) {

        // You should validate the token here using your authentication mechanism
        // For simplicity, let's assume the token is valid

        // Now you can save the profile information to MongoDB using a service
        profileService.saveProfile(profileRequest);

        return ResponseEntity.ok("Profile created successfully");
    }
}
