package com.example.LanguageAppMongoDb.controller.profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class ImageController {

    private static final Logger log = LoggerFactory.getLogger(ImageController.class);

    @GetMapping("/api/profile/image/{imageName}")
    public ResponseEntity<Resource> serveImage(@PathVariable String imageName) throws IOException {

        log.info("Request received for filename: {}", imageName);

        Path imagePath = Paths.get("src/main/resources/static/" + imageName);
        Resource resource = new UrlResource(imagePath.toUri());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG); // Set the appropriate content type for your images

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}

