package com.example.LanguageAppMongoDb.controller.profile;

import com.example.LanguageAppMongoDb.model.profile.Profile;
import com.example.LanguageAppMongoDb.model.users.User;
import com.example.LanguageAppMongoDb.resource.ProfileRequest;
import com.example.LanguageAppMongoDb.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

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

//    @GetMapping
//    public List<Profile> getAllProfiles() {
//        return profileService.getAllProfiles();
//    }

    @GetMapping
    public ResponseEntity<List<ProfileRequest>> getAllProfiles() {
        List<ProfileRequest> profiles = profileService.getAllProfiles().stream()
                .map(profile -> {
                    ProfileRequest request = new ProfileRequest();
                    request.setName(profile.getName());
                    request.setNativeLanguage(profile.getNativeLanguage());
                    request.setLanguageToLearn(profile.getLanguageToLearn());
                    request.setAbout(profile.getAbout());
                    request.setEmail(profile.getEmail());
                    request.setImagePath("/api/profile/image/" + profile.getId());
//                    request.setImageUrl("/api/profile/image/" + profile.getId()); // Set imageUrl
                    request.setImageUrl(profileService.getImageUrl(profile));
                    return request;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(profiles);
    }



    @GetMapping("image/{profileId}")
    public ResponseEntity<byte[]> getImage(@PathVariable String profileId) {
        byte[] imageBytes = profileService.getImageBytes(profileId);
        if (imageBytes != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG); // Adjust the media type based on your image type
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
