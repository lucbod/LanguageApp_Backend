package com.example.LanguageAppMongoDb.repository;

import com.example.LanguageAppMongoDb.model.verification.VerificationToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends MongoRepository<VerificationToken,String> {
    Optional<VerificationToken> findByToken(String token);
}
