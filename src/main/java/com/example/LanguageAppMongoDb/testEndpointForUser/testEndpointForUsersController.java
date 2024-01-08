package com.example.LanguageAppMongoDb.testEndpointForUser;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// endpoint for testing purposes only
@RestController
@RequestMapping("/api/v1/test-controller")
public class testEndpointForUsersController {

    @GetMapping
    @Secured("ROLE_USER")
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hello from public endpoint");
    }
}
