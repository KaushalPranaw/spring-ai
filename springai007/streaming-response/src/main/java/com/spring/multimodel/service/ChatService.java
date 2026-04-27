package com.spring.multimodel.service;

import org.jspecify.annotations.Nullable;
import reactor.core.publisher.Flux;

public interface ChatService {
    String chatTemplate(String query);

    @Nullable Flux<String> streamChat(String query);
}
