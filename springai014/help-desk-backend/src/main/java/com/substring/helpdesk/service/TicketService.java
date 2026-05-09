package com.substring.helpdesk.service;

import com.substring.helpdesk.entity.Ticket;
import com.substring.helpdesk.repository.TicketRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    //create ticket
    @Transactional
    public Ticket createTicket(Ticket ticket) {
        ticket.setId(null);
        if (ticket.getEmail() != null) {
            ticket.setEmail(ticket.getEmail().trim().toLowerCase());
        }
        return ticketRepository.save(ticket);
    }

    //update ticket
    public Ticket updateTicker(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    //get ticket logic
    public Ticket getTicket(long ticketId) {
        return ticketRepository.findById(ticketId).orElse(null);
    }

    //get ticket by email
    @Transactional(readOnly = true)
    public Ticket getTicketByEmailId(String email) {
        if (email == null) return null;
        String normalized = email.trim();
        // prefer case-insensitive lookup
        return ticketRepository.findByEmailIgnoreCase(normalized)
                .or(() -> ticketRepository.findByEmail(normalized))
                .orElse(null);
    }
}
