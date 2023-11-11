package com.example.LanguageAppMongoDb.model.verification;

import com.fasterxml.jackson.databind.annotation.EnumNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.userdetails.User;

import java.util.Date;

import static com.example.LanguageAppMongoDb.model.verification.TokenType.ACCESS_TOKEN;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "access_tokens")
public class AccessToken {
    @Id
    private String id;

    @Field("token")
    private String token;

    private Date expiryDate;

    private String userEmail;

    private User user;

    @Field("tokenType")
    private TokenType tokenType;

    public boolean revoked;

    public boolean expired;

}