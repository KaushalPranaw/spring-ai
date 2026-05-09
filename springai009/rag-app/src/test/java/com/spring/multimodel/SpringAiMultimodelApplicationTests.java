package com.spring.multimodel;

import com.spring.multimodel.helper.Helper;
import com.spring.multimodel.service.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringAiMultimodelApplicationTests {

    @Autowired
    private ChatService chatService;

    @Test
    void saveDataToVectorDatabase(){
        System.out.println("Saving data to vector database");
        this.chatService.saveData(Helper.getData());
        System.out.println("Data is saved successfully");
    }

}
