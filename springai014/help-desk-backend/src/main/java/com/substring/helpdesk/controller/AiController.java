package com.substring.helpdesk.controller;

import com.substring.helpdesk.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping
    public ResponseEntity<String> getResponse(@RequestBody String query,
                                              @RequestHeader(name = "ConversationId") String conversationId) {
        return ResponseEntity.ok(aiService.getResponseFromAssistant(query, conversationId));
    }
}
