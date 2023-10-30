package com.example.LanguageAppMongoDb.auth;

import com.example.LanguageAppMongoDb.model.users.Role;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBuilder {
    private String id;
    private String email;
    private String password;
    private boolean verified;
    private String firstname;
    private String lastname;
    private Role role;
}

