package com.spring.multimodel.controllers;

import com.spring.multimodel.entity.Tut;
import com.spring.multimodel.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class ChatController {

    private ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /*private ChatClient openAiChatClient;
    private ChatClient ollamaChatClient;*/

    //this is not a good practice to create bean here, instead create using AiConfig
    /*public ChatController(OpenAiChatModel openAiChatModel, OllamaChatModel ollamaChatModel) {
        this.openAichatClient = ChatClient.builder(openAiChatModel).build();
        this.ollamaChatClient = ChatClient.builder(ollamaChatModel).build();
    }*/

    //better way
    /*public ChatController(@Qualifier("openAiChatClient") ChatClient openAiChatClient, @Qualifier("ollamaChatClient") ChatClient ollamaChatClient) {
        this.openAiChatClient = openAiChatClient;
        this.ollamaChatClient = ollamaChatClient;
    }*/

    /*public ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }*/

    /*@GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam(value = "q") String query) {
        return ResponseEntity.ok(chatService.chat(query));
    }*/
    /*@GetMapping("/chat")
    public ResponseEntity<Tut> chat(@RequestParam(value = "q") String query) {
        return ResponseEntity.ok(chatService.chatWithEntity(query));
    }*/
    /*@GetMapping("/chat")
    public ResponseEntity<List<Tut>> chat(@RequestParam(value = "q") String query) {
        return ResponseEntity.ok(chatService.chatWithListOfEntity(query));
    }*/

    /*@GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam(value = "q") String query) {
        return ResponseEntity.ok(chatService.chatWithOptions(query));
    }*/

    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam(value = "q") String query) {
        return ResponseEntity.ok(chatService.chatWithPromptTemplate(query));
    }
}
