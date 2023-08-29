package com.ticketseller.service.tickets.exceptions;

public class TicketAlreadyPurchasedException extends Exception {

    public TicketAlreadyPurchasedException(long ticketId) {
        super("Ticket %d has already been purchased".formatted(ticketId));
    }
}
