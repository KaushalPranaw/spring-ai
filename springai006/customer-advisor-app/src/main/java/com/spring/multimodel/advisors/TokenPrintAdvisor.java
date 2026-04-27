package com.spring.multimodel.advisors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import reactor.core.publisher.Flux;

public class TokenPrintAdvisor implements CallAdvisor, StreamAdvisor {

    private Logger logger = LoggerFactory.getLogger(TokenPrintAdvisor.class);

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {

        logger.info("My TokenPrintAdvisor called");
        this.logger.info("Requests:"+chatClientRequest.prompt().getContents());
        ChatClientResponse chatClientResponse=callAdvisorChain.nextCall(chatClientRequest);
        logger.info("TokenAdvisor: Response received from the model");
        this.logger.info(chatClientResponse.chatResponse().getResult().getOutput().getText());
        this.logger.info("Prompt Tokens :"+ chatClientResponse.chatResponse().getMetadata().getUsage().getPromptTokens());
        this.logger.info("Competion Tokens :"+ chatClientResponse.chatResponse().getMetadata().getUsage().getCompletionTokens());
        this.logger.info("Total Tokens Consumed:"+ chatClientResponse.chatResponse().getMetadata().getUsage().getTotalTokens());
        return chatClientResponse;
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {
        return null;
    }
}
