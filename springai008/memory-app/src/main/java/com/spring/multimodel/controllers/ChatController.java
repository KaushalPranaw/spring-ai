package com.spring.multimodel.controllers;

import com.spring.multimodel.entity.Tut;
import com.spring.multimodel.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping
public class ChatController {

    private ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /*@GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam(value = "q") String query) {
        return ResponseEntity.ok(chatService.chatTemplate(query));
    }

    @GetMapping("/stream-chat")
    public ResponseEntity<Flux<String>> streamChat(@RequestParam(value = "q") String query) {
        return ResponseEntity.ok(chatService.streamChat(query));
    }*/

    //ab hume sare user ke liye conversation history store karni hai
    //so we need a conversation id
    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam(value = "q") String query,
                                       @RequestHeader(value = "userId") String userId) {
        return ResponseEntity.ok(chatService.chatTemplateWithConvId(query, userId));
    }

}
