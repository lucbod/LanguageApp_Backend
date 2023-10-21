package com.example.LanguageAppMongoDb.resource;

import com.example.LanguageAppMongoDb.model.chatServer.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {
    private String senderName;
    private String receiverName;
    private String message;

    private String date;
    private Status status;
}
