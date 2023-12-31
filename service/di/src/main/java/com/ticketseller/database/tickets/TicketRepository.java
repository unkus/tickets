package com.ticketseller.database.tickets;

import com.ticketseller.database.CrudRepository;
import com.ticketseller.service.tickets.Ticket;
import com.ticketseller.service.tickets.TicketDetails;
import com.ticketseller.service.tickets.TicketFilter;
import com.ticketseller.service.tickets.exceptions.TicketAlreadyPurchasedException;
import com.ticketseller.service.tickets.exceptions.TicketNotFoundException;
import com.ticketseller.service.users.User;
import com.ticketseller.service.users.exceptions.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TicketRepository extends CrudRepository<Ticket, Long> {
    Page<TicketDetails> getAvailableTickets(Pageable pageable, TicketFilter ticketFilter);
    Page<TicketDetails> getPurchasedTickets(User user, Pageable pageable);
    Optional<TicketDetails> getTicket(long id);

    void purchaseTicket(String login, long ticketId) throws TicketAlreadyPurchasedException, TicketNotFoundException, UserNotFoundException;
}
