package com.substring.helpdesk.repository;

import com.substring.helpdesk.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByEmail(String email);

    // Case-insensitive lookup to tolerate casing differences or surrounding whitespace
    Optional<Ticket> findByEmailIgnoreCase(String email);
}
