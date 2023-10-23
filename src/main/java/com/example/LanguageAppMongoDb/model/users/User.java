package com.example.LanguageAppMongoDb.model.users;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document("users")
public class User {
    private String email;
    private String password;

    private boolean verified;
}
