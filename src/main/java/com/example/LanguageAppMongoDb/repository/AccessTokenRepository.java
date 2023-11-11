package com.example.LanguageAppMongoDb.repository;

import com.example.LanguageAppMongoDb.model.verification.AccessToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccessTokenRepository extends MongoRepository<AccessToken, String> {

    @Query("{ 'userEmail' : ?0, 'expired' : false, 'revoked' : false }")
    void updateTokensToExpiredAndRevoked(String email);

    List<AccessToken>findAllValidTokenByUserEmail(String userEmail);

    //    Optional<AccessToken> findByToken(String token);
Optional<AccessToken> findByToken(String token);
    void deleteByToken(String token);
}
