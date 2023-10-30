package com.example.LanguageAppMongoDb.repository;

import com.example.LanguageAppMongoDb.model.users.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String> {
    Optional<User> findByEmail(String email);
//    Optional<User> findByEmail(String username);
}
