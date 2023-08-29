package com.ticketseller.service.tickets;

import com.ticketseller.database.tickets.TicketRepository;
import com.ticketseller.service.tickets.exceptions.TicketAlreadyPurchasedException;
import com.ticketseller.service.tickets.exceptions.TicketNotFoundException;
import com.ticketseller.service.users.User;
import com.ticketseller.database.users.UserRepository;
import com.ticketseller.service.users.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Primary
@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    private final UserRepository userRepository;

    @Autowired
    public TicketServiceImpl(
            TicketRepository ticketRepository,
            UserRepository userRepository
    ) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<TicketDetails> getAvailableTickets(Pageable pageable, TicketFilter ticketFilter) {
        return ticketRepository.getAvailableTickets(pageable, ticketFilter);
    }

    @Override
    public void purchaseTicket(String login, long ticketId)
            throws UserNotFoundException, TicketNotFoundException, TicketAlreadyPurchasedException {
        // TODO: use transaction
        User user = userRepository.findByLogin(login).orElseThrow(() -> new UserNotFoundException(login));
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new TicketNotFoundException(ticketId));
        if (ticket.getOwnerId() > 0) {
            throw new TicketAlreadyPurchasedException(ticketId);
        }
        ticket.setOwnerId(user.id());
        ticketRepository.update(ticket);
    }

    @Override
    public TicketDetails getTicket(long id) throws TicketNotFoundException {
        return ticketRepository.getTicket(id).orElseThrow(() -> new TicketNotFoundException(id));
    }

}
