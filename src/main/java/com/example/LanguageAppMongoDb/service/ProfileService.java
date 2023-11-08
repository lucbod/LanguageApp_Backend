package com.example.LanguageAppMongoDb.service;

import com.example.LanguageAppMongoDb.model.profile.Profile;
import com.example.LanguageAppMongoDb.model.users.User;
import com.example.LanguageAppMongoDb.repository.ProfileRepository;
import com.example.LanguageAppMongoDb.resource.ProfileRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private MongoTemplate mongoTemplate;


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
// works with file upload
//    public String saveProfilePicture(MultipartFile file) {
//        // !!!!!! good stuff
//         // Retrieve authenticated user from security context
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = (User) authentication.getPrincipal();
//
//        String userId = user.getId();
//
//        // Define the directory where you want to save the pictures
//        String uploadDir = "src/main/resources/static/images"; // Update this with your actual path
//
//        // Create a Path representing the directory
//        Path uploadPath = Paths.get(uploadDir);
//
//        // Ensure the directory exists, if not, create it
//        if (!Files.exists(uploadPath)) {
//            try {
//                Files.createDirectories(uploadPath);
//            } catch (IOException e) {
//                e.printStackTrace();
//                // Handle the exception appropriately
//            }
//        }
//
//        // Generate a unique filename (you might want to use user ID or something)
//        String fileName = userId + "_" + file.getOriginalFilename();
////        System.out.println("filename" + fileName);
//
//        // Create the Path for the file
//        Path filePath = uploadPath.resolve(fileName);
////        System.out.println("filepath" + filePath);
//
//        try {
//            // Save the file to the specified path
//            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//        } catch (IOException e) {
//            e.printStackTrace();
//            // Handle the exception appropriately
//        }
//        return userId + "_" + file.getOriginalFilename();
//    }

    public String saveProfilePicture(MultipartFile file) {
        if (file == null) {
            // No file provided, return the external link for the default image
            return "https://cdn.pixabay.com/photo/2015/06/02/12/59/book-794978_1280.jpg";  // Replace with the actual external link
        }

        // Retrieve authenticated user from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        String userId = user.getId();

        // Define the directory where you want to save the pictures
        String uploadDir = "src/main/resources/static/images"; // Update this with your actual path

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


    public List<Profile> getAllProfiles(String languageToLearn) {
        if (StringUtils.isEmpty(languageToLearn)) {
            return profileRepository.findAll();
        } else {
            return profileRepository.findByLanguageToLearn(languageToLearn);
        }
    }


    public byte[] getImageBytes(String profileId) {
        Optional<Profile> optionalProfile = profileRepository.findById(profileId);
        if (optionalProfile.isPresent()) {
            Profile profile = optionalProfile.get();
            String imagePath = profile.getImagePath();
            // Assuming images are stored in the "src/main/resources/static" directory
            Path imagePathInStaticFolder = Paths.get("src/main/resources/static", imagePath);
            try {
                return Files.readAllBytes(imagePathInStaticFolder);
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception based on your needs
            }
        }
        return null;
    }

    public String getImageUrl(Profile profile) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        String userId = user.getId();
//        String userId = profile.getUserId();  // Replace with the actual method to get user ID
        System.out.println("UserID is: " + userId);

//        String fileName = userId + "_" + profile.getImagePath(); // Adjust as per your actual attributes
        String fileName = profile.getImagePath(); // Adjust as per your actual attributes
        System.out.println("fileName is " + profile.getImagePath());

        System.out.println("filename is" + fileName);
        return "/api/profile/image/" + fileName;
    }

    public List<String> getDistinctLanguages() {
        return mongoTemplate.getCollection("profile")
                .distinct("languageToLearn", String.class)
                .into(new ArrayList<>());
    }
}
