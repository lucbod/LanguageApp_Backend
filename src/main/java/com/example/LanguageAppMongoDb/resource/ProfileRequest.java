package com.example.LanguageAppMongoDb.resource;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProfileRequest {
    private String name;
    private String nativeLanguage;
    private String languageToLearn;
    private String about;
    private String email;

}