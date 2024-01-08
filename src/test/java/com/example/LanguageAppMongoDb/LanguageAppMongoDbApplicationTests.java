package com.example.LanguageAppMongoDb;

import com.example.LanguageAppMongoDb.config.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
class LanguageAppMongoDbApplicationTests {
	@Autowired
	JwtAuthenticationFilter jwtAuthenticationFilter;
	@Test
	void contextLoads() {

		assertNotNull(jwtAuthenticationFilter);
	}

}
