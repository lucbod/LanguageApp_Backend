package com.example.LanguageAppMongoDb.repository;

import com.example.LanguageAppMongoDb.model.verification.AccessToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AccessTokenRepository extends MongoRepository<AccessToken, String> {
    Optional<AccessToken> findByToken(String token);
    void deleteByToken(String token);
}
