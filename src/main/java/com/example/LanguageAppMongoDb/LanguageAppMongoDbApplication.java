package com.example.LanguageAppMongoDb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.LanguageAppMongoDb", "com.example.LanguageAppMongoDb.testEndpointForUsersController"})
public class LanguageAppMongoDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(LanguageAppMongoDbApplication.class, args);
	}

}
