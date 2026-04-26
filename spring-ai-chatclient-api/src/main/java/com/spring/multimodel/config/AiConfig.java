package com.spring.multimodel.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
                .defaultOptions(OpenAiChatOptions
                        .builder()
                        .model("gpt-5-nano")
                        //.temperature(0.3)
                        //.maxTokens(100)
                        .build())
                .build();
    }

}
