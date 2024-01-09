package com.example.LanguageAppMongoDb.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo-controller")
public class DemoController {

    @Value("${context.path:}")
    private String contextPath;

    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String>sayHello(){
        return ResponseEntity.ok("Hey from secured endpoint.This is a context path:"+ contextPath);
    }
}
