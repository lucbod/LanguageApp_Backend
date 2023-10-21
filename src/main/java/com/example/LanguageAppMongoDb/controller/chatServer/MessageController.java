package com.example.LanguageAppMongoDb.controller.chatServer;

import com.example.LanguageAppMongoDb.model.chatServer.Message;
import com.example.LanguageAppMongoDb.model.chatServer.Status;
import com.example.LanguageAppMongoDb.repository.MessageRepository;
import com.example.LanguageAppMongoDb.resource.MessageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class MessageController {

    private final MessageRepository messageRepository;

    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping("/message")
    public ResponseEntity<List<Message>> getAllMessages(){
        return ResponseEntity.ok(this.messageRepository.findAll());
    }

    @PostMapping("/message")
    public ResponseEntity<Message>createMessage(@RequestBody MessageRequest messageRequest){
        Message message = new Message();
        message.setSenderName(messageRequest.getSenderName());
        message.setReceiverName(messageRequest.getReceiverName());
        message.setMessage(messageRequest.getMessage());
        message.setDate(messageRequest.getDate() != null ? messageRequest.getDate() : LocalDateTime.now().toString());
        message.setStatus(messageRequest.getStatus() != null ? messageRequest.getStatus() : Status.DEFAULT_STATUS);

        return ResponseEntity.status(201).body(this.messageRepository.save(message));
    }

    @GetMapping("/message/{id}")
    public ResponseEntity getMessageById(@PathVariable String id){

        Optional<Message> message = this.messageRepository.findById(id);

        if(message.isPresent()){
            return ResponseEntity.ok(message.get());
        } else{
            return ResponseEntity.ok("Message with id:" + id + "was not found");
        }
    }

    @DeleteMapping("/message/{id}")
    public ResponseEntity deleteMessageById(@PathVariable String id){

        Optional<Message> message = this.messageRepository.findById(id);

        if(message.isPresent()){
            this.messageRepository.deleteById(id);
            return ResponseEntity.ok("Success.");
        } else{
            return ResponseEntity.ok("Message with id:" + id + "was not found");
        }
    }
}
