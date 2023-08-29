package com.ticketseller.databse.tickets;

import com.ticketseller.databse.users.UserDao;
import com.ticketseller.service.tickets.Ticket;
import com.ticketseller.service.tickets.TicketDetails;
import com.ticketseller.service.tickets.TicketFilter;
import com.ticketseller.database.tickets.TicketRepository;
import com.ticketseller.service.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Primary
@Repository
public class TicketRepositoryImpl implements TicketRepository {

    private final TicketDao ticketDao;
    private final UserDao userDao;

    @Autowired
    public TicketRepositoryImpl(TicketDao ticketDao, UserDao userDao) {
        this.ticketDao = ticketDao;
        this.userDao = userDao;
    }

    @Override
    public Optional<Ticket> findById(Long id) {
        return Optional.ofNullable(ticketDao.findById(id));
    }

    @Override
    public Page<TicketDetails> getAvailableTickets(Pageable pageable, TicketFilter ticketFilter) {
        return new PageImpl<>(
                ticketDao.getTicketSummaryByOwnerIdIsNull(pageable, ticketFilter),
                pageable,
                ticketDao.getNumberOfAvailableTickets());
    }

    @Override
    public Page<TicketDetails> getPurchasedTickets(User user, Pageable pageable) {
        return new PageImpl<>(
                ticketDao.getTicketsSummaryByOwnerId(user.id()),
                pageable,
                ticketDao.getNumberOfTicketsByOwnerId(user.id())
        );
    }

    @Override
    public Optional<TicketDetails> getTicket(long id) {
        return Optional.ofNullable(ticketDao.getTicketDetailsById(id));
    }

    public Optional<TicketEntity> findById(long id) {
        return Optional.ofNullable(ticketDao.findById(id));
    }

    @Override
    public void create(Ticket user) {
        // Not yet implemented. No requirements to do it.
    }

    @Override
    public TicketEntity read(Long id) {
        return ticketDao.findById(id);
    }

    @Override
    public void update(Ticket ticket) {
        ticketDao.update(ticket);
    }

    @Override
    public void delete(Ticket ticket) {
        // Not yet implemented. No requirements to do it.
    }
}
