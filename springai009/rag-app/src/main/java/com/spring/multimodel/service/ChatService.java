package com.spring.multimodel.service;

import org.jspecify.annotations.Nullable;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ChatService {
    String chatTemplate(String query);

    @Nullable Flux<String> streamChat(String query);
    String chatTemplateWithConvId(String query, String userId);

    void saveData(List<String> data);
    String chatTemplateWithVectorDB(String query, String userId);
}
