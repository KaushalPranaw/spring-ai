package com.spring.multimodel.service;

import com.spring.multimodel.tools.SimpleDataTimeTool;
import com.spring.multimodel.tools.WeatherTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    private WeatherTool weatherTool;

    private ChatClient chatClient;

    public ChatServiceImpl(WeatherTool weatherTool, ChatClient chatClient) {
        this.weatherTool = weatherTool;
        this.chatClient = chatClient;
    }

    //chat method: get response from llm model
    //chatclinet: client for calling llm model
    //tool description: chatbot for tool calling
    @Override
    public String chat(String query) {
        //create a tool
        //attach in chat method
        return chatClient
                .prompt()
                .tools(new SimpleDataTimeTool(), weatherTool)
                .user(query)
                .call()
                .content();
    }
}
