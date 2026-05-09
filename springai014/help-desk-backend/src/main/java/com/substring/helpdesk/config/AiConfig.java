package com.substring.helpdesk.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    private Logger logger= LoggerFactory.getLogger(getClass());

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, ChatMemory chatMemory){

        logger.info("ChatClient bean created");
        logger.info("chat memory bean created: {}", chatMemory.getClass().getName());

        //wahi chiz yaha dalo jo sb jagah use krni ho
        return builder
                .defaultSystem("Summarize the response within 400 words")
                .defaultAdvisors(new SimpleLoggerAdvisor(), MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }


}
