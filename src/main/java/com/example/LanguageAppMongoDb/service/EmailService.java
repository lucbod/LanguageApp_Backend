package com.example.LanguageAppMongoDb.service;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendVerificationEmail(String to, String verificationToken) {
        //customize the email subject and content
        String subject = "Email Verification";
        String verificationLink = "http://your-app-url/verify?token=" + verificationToken; // Adjust the URL

        String content = "Click the following link to verify your email: " + verificationLink;

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

        javaMailSender.send(message);
    }
}
