package com.example.LanguageAppMongoDb.resource;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegistrationRequest {
    private String email;
    private String password;

    private boolean verified;

}
