package com.spring.multimodel.service;

import com.spring.multimodel.entity.Tut;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChatServiceImpl implements ChatService {

    private ChatClient chatClient;
    @Value("classpath:/prompts/user-message.st")
    private Resource userMessageResource;

    @Value("classpath:/prompts/system-message.st")
    private Resource systemMessageResource;


    /*public ChatServiceImpl(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }*/
    /*public ChatServiceImpl(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultOptions(OpenAiChatOptions
                        .builder()
                        .model("gpt-5-nano")
                        //.temperature(0.3)
                        //.maxTokens(100)
                        .build())
                .build();
    }*/
    //but avi upar wala default option is for this class specific
    //we can move this to AiConfig class and create bean for chat client
    // with default options and use that bean here
    public ChatServiceImpl(ChatClient chatClient) {
        //already bean is created in AiConfig class, so use directly here
        this.chatClient = chatClient;
    }

    @Override
    public String chat(String query) {

        String prompt = "tell me about virat kohli?";
        //call the llm
        var content =
                chatClient
                        .prompt()
                        .user(prompt)
                        .system("as an expert in cricket")
                        .call()
                        .content();
        return content;

        //how to use prompt object
        /*String promptMsg = "tell me about virat kohli?";
        Prompt promptObj = new Prompt(promptMsg);
        var content = chatClient
                .prompt(promptObj)
                .call()
                .content();
        return content;*/

        //detailed response chahiye to use ChatResponse object
        /*String promptMsg = "tell me about virat kohli?";
        Prompt promptObj = new Prompt(promptMsg);
        var content = chatClient
                .prompt(promptObj)
                .call()
                .chatResponse()
                .getResult()
                .getOutput()
                .getText();//aur bchize kr skte hai
        return content;*/

        //for getting metadata and other details we can use ChatResponse object
        /*String promptMsg = "tell me about virat kohli?";
        Prompt promptObj = new Prompt(promptMsg);
        var metadata = chatClient
                .prompt(promptObj)
                .call()
                .chatResponse()
                .getMetadata();
        System.out.println(metadata);
        return "";*/
    }

    @Override
    public Tut chatWithEntity(String query) {
        //let suppose we want to return entity from the response then we can use below code
        Prompt promptObj = new Prompt(query);
        Tut tutorial = chatClient
                .prompt(promptObj)
                .call()
                .entity(Tut.class);
        return tutorial;


    }

    @Override
    public List<Tut> chatWithListOfEntity(String query) {
        //now suppose we want to return list of entities then we can use below code
        Prompt promptObj = new Prompt(query);
        List<Tut> tutorials = chatClient
                .prompt(promptObj)
                .call()
                .entity(new ParameterizedTypeReference<List<Tut>>() {
                });
        return tutorials;

        //now suppose we want to return list of entities then we can use below code

    }

    @Override
    public String chatWithOptions(String query) {
        /*Prompt promptObj = new Prompt(
                query,
                ChatOptions
                        .builder()
                        .model("gpt-5-nano")
                        //.temperature(0.3)
                        //.maxTokens(100)
                        .build()
                        );
                    var content = chatClient
                            .prompt(promptObj)
                            .call()
                            .content();
                    return content;*/

        //in above it is prompt specific, instead we can use this while creating chat client
        Prompt promptObj = new Prompt(
                query
        );
        var content = chatClient
                .prompt(promptObj)
                .call()
                .content();
        return content;

    }

    @Override
    public String chatWithPromptTemplate(String query) {
        Prompt promptObj = new Prompt(query);
        //modify the prompt and add some extra things to make it more interactive
        //one way is like below but not suitable for all the queries
        String queryStr = "As an expert in coding and programming. Always write code in Java. Now reply for this query: {query}";

        return chatClient
                .prompt()
                .user(u -> u.text(queryStr).param("query", query))
                .call()
                .content();
        //instead of doing all things under user(), we can create prompt template and use that template.
        //lets do in another below method
    }

    public String chatTemplate() {

        //Step 1: Create a prompt template
        PromptTemplate strTemplate = PromptTemplate
                .builder()
                .template("what is {techName}. tell me example of {exampleName}")
                .build();

        //Step 2: render the template with actual values
        //ye TemplateRenderer ki help sehota hai
        String renderMsg = strTemplate.render(Map.of(
                "techName", "java",
                "exampleName", "spring boot"
        ));

        //Step 3: create prompt object with rendered message
        Prompt prompt = new Prompt(renderMsg);

        //Step 4: call the llm
        var content = chatClient.prompt(prompt).call().content();
        return content;

    }

    public String chatTemplate2() {
        //Step 1: Create a prompt template
        //Step 2: render the template with actual values
        //ye TemplateRenderer ki help sehota hai
        //Step 3: create prompt object with rendered message
        //Step 4: call the llm

        SystemPromptTemplate systemPromptTemplate =
                SystemPromptTemplate
                        .builder()
                        .template("you are an helpful coding assistant. you are an expert in coding")
                        .build();
        Message systemMsg = systemPromptTemplate.createMessage();
        PromptTemplate userTemplate = PromptTemplate
                .builder()
                .template("what is {techName}. tell me example of {techExample}")
                .build();
        Message userMsg = userTemplate.createMessage(Map.of(
                "techName", "spring",
                "techExample", "spring exception"
        ));

        Prompt prompt = new Prompt(List.of(systemMsg, userMsg));
        return this.chatClient.prompt(prompt).call().content();

    }

    public String chatFluentChatApi() {
        return this.chatClient
                .prompt()
                .system(system -> system.text("you are an helpful coding assistant. you are an expert in coding"))
                .user(user -> user.text("what is {techName}. tell me example of {techExample}").param("techName", "spring").param("techExample", "spring exception"))
                .call()
                .content();

    }

    public String chatFluentChatApiUsingFileRead() {
        return this.chatClient
                .prompt()
                .system(system -> system.text(systemMessageResource))
                .user(user ->
                        user
                        .text(userMessageResource)
                        .param("concept", "spring framework validation")
                )
                .call()
                .content();

    }
}
