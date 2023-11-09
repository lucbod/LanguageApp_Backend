package com.example.LanguageAppMongoDb.service;

import com.example.LanguageAppMongoDb.model.verification.AccessToken;
import com.example.LanguageAppMongoDb.repository.AccessTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccessTokenService {

    private final AccessTokenRepository accessTokenRepository;

    @Autowired
    public AccessTokenService(AccessTokenRepository accessTokenRepository) {
        this.accessTokenRepository = accessTokenRepository;
    }

    public void saveAccessToken(AccessToken accessToken) {
        accessTokenRepository.save(accessToken);
    }

    public Optional<AccessToken> findAccessTokenByToken(String token) {
        return accessTokenRepository.findByToken(token);
    }

    public void deleteAccessToken(String token) {
        accessTokenRepository.deleteByToken(token);
    }
}
