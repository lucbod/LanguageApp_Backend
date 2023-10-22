package com.example.LanguageAppMongoDb.model.chatServer;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document("messages")
public class Message {

    @Id
    private String id;

    private String senderName;
    private String receiverName;
    private String message;

    private String date = LocalDateTime.now().toString();
    private Status status = Status.DEFAULT_STATUS;

}
