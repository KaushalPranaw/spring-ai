package com.spring.multimodel.config;

import com.spring.multimodel.advisors.TokenPrintAdvisor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AiConfig {

    private Logger logger = LoggerFactory.getLogger(AiConfig.class);

    @Bean
    public ChatClient openAiChatClient(ChatClient.Builder builder, ChatMemory chatMemory) {

        this.logger.info("ChatMemoryImplementation class: " + chatMemory.getClass().getName());

        //using this advisor to store the conversation in memory and use it for future reference.
        MessageChatMemoryAdvisor messageChatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();

        return builder
                .defaultAdvisors(messageChatMemoryAdvisor, new SimpleLoggerAdvisor(), new SafeGuardAdvisor(List.of("games")))
                .defaultOptions(OpenAiChatOptions
                        .builder()
                        .model("gpt-4o-mini")
                        .temperature(0.3)
                        .maxTokens(200)
                        .build())
                .build();
    }

}
