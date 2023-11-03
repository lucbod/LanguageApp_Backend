package com.example.LanguageAppMongoDb.service;

import com.example.LanguageAppMongoDb.model.profile.Profile;
import com.example.LanguageAppMongoDb.model.users.User;
import com.example.LanguageAppMongoDb.repository.ProfileRepository;
import com.example.LanguageAppMongoDb.resource.ProfileRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    public void saveProfile(ProfileRequest profileRequest) {
        Profile profile = Profile.builder()
//                .id(profileRequest.getId())
                .name(profileRequest.getName())
                .nativeLanguage(profileRequest.getNativeLanguage())
                .languageToLearn(profileRequest.getLanguageToLearn())
                .about(profileRequest.getAbout())
                .email(profileRequest.getEmail())
                .imagePath(profileRequest.getImagePath())
                .build();
        // You can add additional logic or validation here before saving

        profileRepository.save(profile);
    }

    public String saveProfilePicture(MultipartFile file) {
        // !!!!!! good stuff
         // Retrieve authenticated user from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        String userId = user.getId();

        // Define the directory where you want to save the pictures
        String uploadDir = "src/main/resources/static"; // Update this with your actual path

        // Create a Path representing the directory
        Path uploadPath = Paths.get(uploadDir);

        // Ensure the directory exists, if not, create it
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception appropriately
            }
        }

        // Generate a unique filename (you might want to use user ID or something)
        String fileName = userId + "_" + file.getOriginalFilename();

        // Create the Path for the file
        Path filePath = uploadPath.resolve(fileName);

        try {
            // Save the file to the specified path
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }
        return userId + "_" + file.getOriginalFilename();
    }
    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }
}
