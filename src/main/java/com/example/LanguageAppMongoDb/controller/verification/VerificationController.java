package com.example.LanguageAppMongoDb.controller.verification;

import com.example.LanguageAppMongoDb.resource.VerificationTokenRequest;
import com.example.LanguageAppMongoDb.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VerificationController {

    private final UserService userService;

    public VerificationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/verify-token")
    public ResponseEntity<String> verifyUserByToken(@RequestBody VerificationTokenRequest tokenRequest) {
        String token = tokenRequest.getToken();
        boolean verificationSuccessful = userService.verifyUser(token);

        if (verificationSuccessful) {
            return ResponseEntity.ok("Verification successful");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verification failed");
        }
    }

//    @GetMapping("/verify")
//    public String verifyUser(@RequestParam("token") String token) {
//        boolean verificationSuccessful = userService.verifyUser(token);
//
//        // Step 5: Redirect the user to a page indicating whether the verification was successful
//        if (verificationSuccessful) {
//            return "redirect:/verification-status?success=true";
//        } else {
//            return "redirect:/verification-status?success=false";
//        }
//    }
}

