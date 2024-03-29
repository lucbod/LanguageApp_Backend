package com.example.LanguageAppMongoDb.controller.profile;

import com.example.LanguageAppMongoDb.model.profile.Profile;
import com.example.LanguageAppMongoDb.model.users.User;
import com.example.LanguageAppMongoDb.resource.ProfileRequest;
import com.example.LanguageAppMongoDb.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ResourceLoader resourceLoader;

    // works with file upload
//    @PostMapping("/create")
//    public ResponseEntity<String> createProfile(
//            @RequestHeader("Authorization") String token,
//            @RequestParam(value = "image", required = false) MultipartFile file,
//            @ModelAttribute ProfileRequest profileRequest) {
//
//        // You should validate the token here using your authentication mechanism
//        // For simplicity, let's assume the token is valid
//
//        // Handle profile picture upload if the file is present
//        if (file != null) {
//            String imagePath = profileService.saveProfilePicture(file);
//            profileRequest.setImagePath(imagePath);
//        }
//
//        // Now you can save the profile information to MongoDB using a service
//        profileService.saveProfile(profileRequest);
//
//        return ResponseEntity.ok("Profile created successfully");
//    }

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
        } else {
            // No file provided, set the external link for the default image
            profileRequest.setImagePath("https://cdn.pixabay.com/photo/2015/06/02/12/59/book-794978_1280.jpg");  // Replace with the actual external link
        }

        // Now you can save the profile information to MongoDB using a service
        profileService.saveProfile(profileRequest);

        return ResponseEntity.ok("Profile created successfully");
    }


//    @GetMapping
//    public List<Profile> getAllProfiles() {
//        return profileService.getAllProfiles();
//    }

    @GetMapping("/languages")
    public ResponseEntity<List<String>> getDistinctLanguages() {
        List<String> languages = profileService.getDistinctLanguages();
        return ResponseEntity.ok(languages);
    }
//    @GetMapping
//    public ResponseEntity<List<ProfileRequest>> getAllProfiles() {
//        List<ProfileRequest> profiles = profileService.getAllProfiles().stream()
//                .map(profile -> {
//                    ProfileRequest request = new ProfileRequest();
//                    request.setName(profile.getName());
//                    request.setNativeLanguage(profile.getNativeLanguage());
//                    request.setLanguageToLearn(profile.getLanguageToLearn());
//                    request.setAbout(profile.getAbout());
//                    request.setEmail(profile.getEmail());
//                    request.setImagePath("/api/profile/image/" + profile.getId());
//
////                    request.setImageUrl("/api/profile/image/" + profile.getId()); // Set imageUrl
//                    request.setImageUrl(profileService.getImageUrl(profile));
//                    return request;
//                })
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(profiles);
//    }

    @GetMapping
    public ResponseEntity<List<ProfileRequest>> getAllProfiles(
            @RequestParam(required = false) String languageToLearn) {
        List<ProfileRequest> profiles = profileService.getAllProfiles(languageToLearn).stream()
                .map(profile -> {
                    ProfileRequest request = new ProfileRequest();
                    request.setName(profile.getName());
                    request.setNativeLanguage(profile.getNativeLanguage());
                    request.setLanguageToLearn(profile.getLanguageToLearn());
                    request.setAbout(profile.getAbout());
                    request.setEmail(profile.getEmail());
                    request.setImagePath("/api/profile/image/" + profile.getId());
                    request.setImageUrl(profileService.getImageUrl(profile));
                    return request;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(profiles);
    }


//    @GetMapping("image/{profileId}")
//    public ResponseEntity<byte[]> getImage(@PathVariable String profileId) {
//        byte[] imageBytes = profileService.getImageBytes(profileId);
//        if (imageBytes != null) {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.IMAGE_PNG); // Adjust the media type based on your image type
//            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }

    @GetMapping("/image/{profileId}")
    public ResponseEntity<Resource> getImage(@PathVariable String profileId) throws IOException {
        byte[] imageBytes = profileService.getImageBytes(profileId);
        if (imageBytes != null) {
//            Path imagePath = Paths.get("src/main/resources/static/images/" + profileId); // Adjust the path accordingly
            Path imagePath = Paths.get("src/main/resources/static/" + profileId);

            Resource resource = new UrlResource(imagePath.toUri());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



}
