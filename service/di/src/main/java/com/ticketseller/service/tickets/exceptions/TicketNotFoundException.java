package com.ticketseller.service.tickets.exceptions;

public class TicketNotFoundException extends Exception {
    public TicketNotFoundException(long id) {
        super("Ticket %d not found".formatted(id));
    }
}
