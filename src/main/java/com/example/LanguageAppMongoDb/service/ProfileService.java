package com.example.LanguageAppMongoDb.service;

import com.example.LanguageAppMongoDb.model.profile.Profile;
import com.example.LanguageAppMongoDb.repository.ProfileRepository;
import com.example.LanguageAppMongoDb.resource.ProfileRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    public void saveProfile(ProfileRequest profileRequest) {
        Profile profile = Profile.builder()
                .name(profileRequest.getName())
                .nativeLanguage(profileRequest.getNativeLanguage())
                .languageToLearn(profileRequest.getLanguageToLearn())
                .about(profileRequest.getAbout())
                .email(profileRequest.getEmail())
                .build();

        // You can add additional logic or validation here before saving

        profileRepository.save(profile);
    }
}
