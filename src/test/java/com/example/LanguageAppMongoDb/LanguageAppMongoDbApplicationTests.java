package com.example.LanguageAppMongoDb;

import com.example.LanguageAppMongoDb.config.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.bson.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class LanguageAppMongoDbApplicationTests {
	JwtAuthenticationFilter jwtAuthenticationFilter;
	@Test
	void contextLoads() {

		assertNotNull(jwtAuthenticationFilter);
	}

}
