package com.example.LanguageAppMongoDb.repository;

import com.example.LanguageAppMongoDb.model.profile.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ProfileRepository extends MongoRepository<Profile,String> {

}