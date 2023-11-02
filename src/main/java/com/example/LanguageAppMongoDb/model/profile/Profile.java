package com.example.LanguageAppMongoDb.model.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("profile")
public class Profile {
    @Id
    private String id;
    private String name;
    private String nativeLanguage;
    private String languageToLearn;
    private String about;
    private String email;

}
