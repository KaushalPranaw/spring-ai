package com.spring.multimodel.service;

import com.spring.multimodel.entity.Tut;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

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

    /*
    //for specific request if want to apply advisor(logger)
    public String chatTemplate(String query) {
        return this.chatClient
                .prompt()
                .advisors(new SimpleLoggerAdvisor())
                .system(system -> system.text(systemMessageResource))
                .user(user ->
                        user.text(userMessageResource)
                                .param("concept", query))
                .call()
                .content();
    }*/

    //if want to apply logger advisor globally for all request
    public String chatTemplate(String query) {
        return this.chatClient
                .prompt()
                .advisors(new SimpleLoggerAdvisor()) // enable per-request advisor to ensure logs appear
                //.advisors(new SimpleLoggerAdvisor())
                .system(system -> system.text(systemMessageResource))
                .user(user ->
                        user.text(userMessageResource)
                                .param("concept", query))
                .call()
                .content();
    }

    @Override
    public @Nullable Flux<String> streamChat(String query) {
        return this.chatClient
                .prompt()
                .system(system -> system.text(systemMessageResource))
                .user(user -> user.text(userMessageResource).param("concept", query))
                .stream()
                .content();
    }

    @Override
    public String chatTemplateWithConvId(String query, String userId) {
        return this.chatClient
                .prompt()
                .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID, userId))
                .system(system -> system.text(systemMessageResource))
                .user(user -> user.text(userMessageResource).param("concept", query))
                .call().content();
    }

    @Override
    public void saveData(List<String> list) {
        List<Document> documentList = list.stream().map(item -> new Document(item)).toList();
        this.vectorStore.accept(documentList);
    }

    /*@Override
    public String chatTemplateWithVectorDB(String query, String userId) {
        //load data from vector db
        SearchRequest searchRequest = SearchRequest.builder()
                .topK(3)//3 best result dega. bhut jyada nhi dena chhaiye, pricing badh jayegi
                .similarityThreshold(0.6)//means bich ka
                .query(query)
                .build();
        List<Document> documents = this.vectorStore.similaritySearch(searchRequest);
        List<@Nullable String> documentList = documents.stream().map(document -> document.getText()).toList();
        String contextData = String.join(", ", documentList);
        logger.info("Context data: {}", contextData);

        //similar result from user query
        //pass in query or context
        return this.chatClient
                .prompt()
                .system(system -> system.text(this.systemMessageResource).param("documents", contextData))
                .user(user -> user.text(this.userMessageResource).param("query", query))
                .call()
                .content();
    }*/

    //using avi manually hum load kr rhe te from vector db
    //and topK and similarity de rhe teh and passing in system msg

    //ye sb ab hum QuestionAnswerAdvisor se krege
    /*@Override
    public String chatTemplateWithVectorDB(String query, String userId) {
        //load data from vector db
        *//*SearchRequest searchRequest = SearchRequest.builder()
                .topK(3)//3 best result dega. bhut jyada nhi dena chhaiye, pricing badh jayegi
                .similarityThreshold(0.6)//means bich ka
                .query(query)
                .build();
        List<Document> documents = this.vectorStore.similaritySearch(searchRequest);
        List<@Nullable String> documentList = documents.stream().map(document -> document.getText()).toList();
        String contextData = String.join(", ", documentList);
        logger.info("Context data: {}", contextData);*//*

        //similar result from user query
        //pass in query or context
        return this.chatClient
                .prompt()
                //.system(system -> system.text(this.systemMessageResource).param("documents", contextData))
                //.advisors(QuestionAnswerAdvisor.builder(vectorStore).build())
                //yaah b similariry search kr skte h
                .advisors(QuestionAnswerAdvisor.builder(vectorStore)
                        .searchRequest(SearchRequest.builder().topK(3).similarityThreshold(0.6).build()).build())
                .user(user -> user.text(this.userMessageResource).param("query", query))
                .call()
                .content();
    }*/

    //now lets try to use RetrievalAugmentationAdvisor
    //ye jyada advance hai like pre-retrival etc
    //this is most imp Advisor
    @Override
    public String chatTemplateWithVectorDB(String query, String userId) {
        //load data from vector db
        //similar result from user query
        //pass in query or context

        /*RetrievalAugmentationAdvisor retrievalAugmentationAdvisor
                = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .vectorStore(vectorStore)
                        .topK(3)
                        .similarityThreshold(0.5)
                        .build())
                .build();*/
        RetrievalAugmentationAdvisor retrievalAugmentationAdvisor
                = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .vectorStore(vectorStore)
                        .topK(3)
                        .similarityThreshold(0.5)
                        .build())
                .queryAugmenter(ContextualQueryAugmenter.builder().allowEmptyContext(true).build())
                .build();

        return this.chatClient
                .prompt()
                //.system(system -> system.text(this.systemMessageResource).param("documents", contextData))
                //.advisors(QuestionAnswerAdvisor.builder(vectorStore).build())
                //yaah b similariry search kr skte h
                //.advisors(QuestionAnswerAdvisor.builder(vectorStore)
                  //      .searchRequest(SearchRequest.builder().topK(3).similarityThreshold(0.6).build()).build())


                .advisors(retrievalAugmentationAdvisor)
                .user(user -> user.text(this.userMessageResource).param("query", query))
                .call()
                .content();
    }

}
