package com.spring.multimodel.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AiConfig {

//    @Bean(name = "openAiChatClient")
//    public ChatClient openAiChatModel(OpenAiChatModel openAiChatModel){
//        return ChatClient.builder(openAiChatModel).build();
//    }
//
//    @Bean(name = "ollamaChatClient")
//    public ChatClient ollamaChatModel(OllamaChatModel ollamaChatModel){
//        return ChatClient.builder(ollamaChatModel).build();
//    }

    @Bean
    public ChatClient openAiChatClient(ChatClient.Builder builder){
        return builder
                .defaultAdvisors(new SimpleLoggerAdvisor(), new SafeGuardAdvisor(List.of("games")))
                .defaultOptions(OpenAiChatOptions
                        .builder()
                        .model("gpt-4o-mini")
                        .temperature(0.3)
                        .maxTokens(200)
                        .build())
                .build();
    }

}
