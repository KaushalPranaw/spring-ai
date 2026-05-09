package com.spring.multimodel.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//first tool we are creating
public class SimpleDataTimeTool {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //information tool

    @Tool(description = "Get the current date and time in users zone.")
    public String getCurrentDateTime() {
        logger.info("Tool calling");
        logger.info("Get the current date and time in users zone.");
        return LocalDateTime.now()
                .atZone(LocaleContextHolder.getTimeZone().toZoneId())
                .toString();
    }

    //action tool: that I can take action
    @Tool(description = "Set the alarm for given time.")
    void setAlarm(@ToolParam(description = "Time in ISO-8601 format") String time) {
        LocalDateTime dateTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME);
        logger.info("Set the alarm for given time. {}", dateTime);
    }

}
