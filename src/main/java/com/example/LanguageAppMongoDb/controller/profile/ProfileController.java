package com.example.LanguageAppMongoDb.controller.profile;

import com.example.LanguageAppMongoDb.model.profile.Profile;
import com.example.LanguageAppMongoDb.model.users.User;
import com.example.LanguageAppMongoDb.resource.ProfileRequest;
import com.example.LanguageAppMongoDb.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PostMapping("/create")
    public ResponseEntity<String> createProfile(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "image", required = false) MultipartFile file,
            @ModelAttribute ProfileRequest profileRequest) {

        // You should validate the token here using your authentication mechanism
        // For simplicity, let's assume the token is valid

        // Handle profile picture upload if the file is present
        if (file != null) {
            String imagePath = profileService.saveProfilePicture(file);
            profileRequest.setImagePath(imagePath);
        }

        // Now you can save the profile information to MongoDB using a service
        profileService.saveProfile(profileRequest);

        return ResponseEntity.ok("Profile created successfully");
    }

    @GetMapping
    public List<Profile> getAllProfiles() {
        return profileService.getAllProfiles();
    }
}
