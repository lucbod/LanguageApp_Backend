package com.example.LanguageAppMongoDb.controller.chatServer;

import com.example.LanguageAppMongoDb.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageRepository messageRepository;


    @Autowired
    public ChatController(SimpMessagingTemplate simpMessagingTemplate, MessageRepository messageRepository) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.messageRepository = messageRepository;
    }

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    private com.example.LanguageAppMongoDb.model.chatServer.Message receivePublicMessage
            (@Payload com.example.LanguageAppMongoDb.model.chatServer.Message message){
        messageRepository.save(message);
        return message;
    }

    @MessageMapping("/private-message")
    public com.example.LanguageAppMongoDb.model.chatServer.Message receivePrivateMessage
            (@Payload com.example.LanguageAppMongoDb.model.chatServer.Message message){

        messageRepository.save(message);

        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private", message); // /user/David/private
        return message;
    }
}

