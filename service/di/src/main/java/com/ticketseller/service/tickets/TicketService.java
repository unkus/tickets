package com.ticketseller.service.tickets;

import com.ticketseller.service.tickets.exceptions.TicketAlreadyPurchasedException;
import com.ticketseller.service.tickets.exceptions.TicketNotFoundException;
import com.ticketseller.service.users.exceptions.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TicketService {
    Page<TicketDetails> getAvailableTickets(Pageable pageable, TicketFilter ticketFilter);

    void purchaseTicket(String login, long ticketId)
            throws UserNotFoundException, TicketNotFoundException, TicketAlreadyPurchasedException;

    TicketDetails getTicket(long id) throws TicketNotFoundException;
}
