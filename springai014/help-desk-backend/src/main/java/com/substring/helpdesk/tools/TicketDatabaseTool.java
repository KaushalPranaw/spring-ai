package com.substring.helpdesk.tools;

import com.substring.helpdesk.entity.Ticket;
import com.substring.helpdesk.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketDatabaseTool {

    private final TicketService ticketService;
    private static final Logger log = LoggerFactory.getLogger(TicketDatabaseTool.class);

    //create ticket tool
    @Tool(description = "This tool helps to create new ticket in database.", returnDirect = true)
    public Ticket createTicketTool(@ToolParam(description = "Ticket fields required to create new ticket") Ticket ticket) {
        try {
            log.debug("going to create ticket: {}", ticket);
            Ticket saved = ticketService.createTicket(ticket);
            // force materialization of summary to avoid LOB issues
            if (saved != null) {
                String _summary = saved.getSummary();
                log.debug("materialized summary length={}", _summary == null ? 0 : _summary.length());
            }
            return saved;
        } catch (Exception e) {
            log.error("error creating ticket", e);
            return null;
        }
    }

    //get ticket using email
    @Tool(description = "this tool helps to get ticket by email", returnDirect = true)
    public Ticket getTicketByEmailId(@ToolParam(description = "email id whose ticket is required") String emailId){
        log.debug("getTicketByEmailId called with raw email='{}'", emailId);
        Ticket t = ticketService.getTicketByEmailId(emailId);
        if (t == null) {
            log.debug("No ticket found for email='{}'", emailId);
            return null;
        } else {
            // touch the summary to ensure it's loaded while session is open
            String _summary = t.getSummary();
            log.debug("materialized summary length={}", _summary == null ? 0 : _summary.length());
            log.debug("Found ticket for email='{}': id={}", emailId, t.getId());
            return t;
        }
    }

    @Tool(description = "This tool helps to update ticket.", returnDirect = true)
    public Ticket updateTicket(@ToolParam(description = "new ticket details with ticket id") Ticket ticket){
        Ticket updated = ticketService.updateTicker(ticket);
        if (updated != null) {
            String _summary = updated.getSummary();
            log.debug("materialized summary length={}", _summary == null ? 0 : _summary.length());
        }
        return updated;
    }

    //get current system time
    @Tool(description = "This tool helps to get current system time.", returnDirect = true)
    public String getCurrentTime(){
        return String.valueOf(System.currentTimeMillis());
    }
}
