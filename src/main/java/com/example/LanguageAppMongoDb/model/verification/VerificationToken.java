package com.example.LanguageAppMongoDb.model.verification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document("verification_tokens")
public class VerificationToken {

    @Id
    private String id;

    private String token;

    private LocalDateTime expiryDate;

    // Associate the token with the user's email
    private String userEmail;

}