package com.spring.multimodel.service;

import com.spring.multimodel.entity.Tut;

import java.util.List;

public interface ChatService {
    String chat(String query);
    Tut chatWithEntity(String query);
    List<Tut> chatWithListOfEntity(String query);
    String chatWithOptions(String query);
    String chatWithPromptTemplate(String query);
    String chatTemplate();
    String chatTemplate2();
    String chatFluentChatApi();
    String chatFluentChatApiUsingFileRead();
}
