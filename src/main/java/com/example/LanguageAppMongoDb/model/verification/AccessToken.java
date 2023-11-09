package com.example.LanguageAppMongoDb.model.verification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "access_tokens")
public class AccessToken {
    @Id
    private String id;

    private String token;

    private Date expiryDate;

    private String userEmail;
}