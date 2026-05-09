package com.spring.multimodel.service;

import org.jspecify.annotations.Nullable;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ChatService {
    String chat(String query);
}
