package com.spring.multimodel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.join.ConcatenationDocumentJoiner;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ChatServiceImpl implements ChatService {

    private ChatClient chatClient;
    @Value("classpath:/prompts/user-message.st")
    private Resource userMessageResource;

    @Value("classpath:/prompts/system-message.st")
    private Resource systemMessageResource;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private VectorStore vectorStore;

    public ChatServiceImpl(ChatClient chatClient, VectorStore vectorStore) {

        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

    @Override
    public void saveData(List<String> list) {
        List<Document> documentList = list.stream().map(item -> new Document(item)).toList();
        this.vectorStore.accept(documentList);
    }

    @Override
    public String getResponse(String userQuery) {

        var advisor= RetrievalAugmentationAdvisor.builder()
                //pre retrieval phase
                .queryTransformers(
                        RewriteQueryTransformer.builder()
                                .chatClientBuilder(chatClient.mutate().clone())
                                .build(),
                        //man lo hindi me query aa rhi h to convert into english use TransalationQueryTransformer
                        //vaise apne ap use kr leta hai
                        TranslationQueryTransformer.builder()
                                .chatClientBuilder(chatClient.mutate().clone())
                                .targetLanguage("english")
                                .build()

                )
                .queryExpander(MultiQueryExpander.builder()
                        .chatClientBuilder(chatClient.mutate().clone())
                        .numberOfQueries(3)
                        .build()
                )
                //retrieval phase
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .vectorStore(vectorStore)
                        .topK(3)
                        .similarityThreshold(0.3)
                        .build())
                //post retrieval
                .documentJoiner(new ConcatenationDocumentJoiner())
                //generation
                .queryAugmenter(ContextualQueryAugmenter.builder().build())
                .build();

        return this.chatClient
                .prompt()
                .advisors(advisor)
                .user(userQuery)
                .call().content();
    }

}
