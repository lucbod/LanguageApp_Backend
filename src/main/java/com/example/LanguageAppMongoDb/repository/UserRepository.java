package com.example.LanguageAppMongoDb.repository;

import com.example.LanguageAppMongoDb.model.users.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User,String> {
}
