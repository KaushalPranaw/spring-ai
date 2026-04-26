package com.spring.multimodel.service;

import com.spring.multimodel.entity.Tut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatClient chatClient;
    private static final Logger log = LoggerFactory.getLogger(ChatServiceImpl.class);

    @Value("classpath:/prompts/user-message.st")
    private Resource userMessageResource;

    @Value("classpath:/prompts/system-message.st")
    private Resource systemMessageResource;

    public ChatServiceImpl(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    /*
    //for specific request if want to apply advisor(logger)
    public String chatTemplate(String query) {
        return this.chatClient
                .prompt()
                .advisors(new SimpleLoggerAdvisor())
                .system(system -> system.text(systemMessageResource))
                .user(user ->
                        user.text(userMessageResource)
                                .param("concept", query))
                .call()
                .content();
    }*/

    //if want to apply logger advisor globally for all request
    public String chatTemplate(String query) {
        log.info("chatTemplate invoked with query={}", query);
        return this.chatClient
                .prompt()
                .advisors(new SimpleLoggerAdvisor()) // enable per-request advisor to ensure logs appear
                //.advisors(new SimpleLoggerAdvisor())
                .system(system -> system.text(systemMessageResource))
                .user(user ->
                        user.text(userMessageResource)
                                .param("concept", query))
                .call()
                .content();
    }
}
