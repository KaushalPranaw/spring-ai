package com.substring.helpdesk.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class EmailTool {

    //send email to support team regarding the ticket
    @Tool(description = "This tool helps to send email to support team regarding the ticket.")
    public void sendEmailToSupportTeam(@ToolParam(description = "email id associated with ticket for contact information") String email,
                                       @ToolParam(description = "short description of ticket summary") String message){
        //sending email to support team
        System.out.println("going to send email to support team");
        System.out.println("email: " + email);
        System.out.println("message: " + message);
    }
}
