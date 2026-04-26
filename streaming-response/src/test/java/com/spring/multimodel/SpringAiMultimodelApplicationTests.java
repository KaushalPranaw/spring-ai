package com.spring.multimodel;

import com.spring.multimodel.service.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringAiMultimodelApplicationTests {

    @Autowired
    private ChatService chatService;

	@Test
	void contextLoads() {
	}

    /*@Test
    void testTemplateRenderer(){
        System.out.println("Testing template renderer...");
        var output=this.chatService.chatTemplate();
        System.out.println(output);
    }*/
    /*@Test
    void testChatTemplate2(){
        System.out.println("Testing template renderer...");
        var output=this.chatService.chatTemplate2();
        System.out.println(output);
    }*/

    /*@Test
    void testChatFluentChatApi(){
        System.out.println("Testing template renderer...");
        var output=this.chatService.chatFluentChatApi();
        System.out.println(output);
    }*/

    @Test
    void testChatFluentChatApiUsingFileRead(){
        System.out.println("Testing template renderer...");
        var output=this.chatService.chatFluentChatApiUsingFileRead();
        System.out.println(output);
    }

}
