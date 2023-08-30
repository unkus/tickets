package com.ticketseller.databse.tickets;

import com.ticketseller.databse.users.UserDao;
import com.ticketseller.service.tickets.Ticket;
import com.ticketseller.service.tickets.TicketDetails;
import com.ticketseller.service.tickets.TicketFilter;
import com.ticketseller.database.tickets.TicketRepository;
import com.ticketseller.service.tickets.exceptions.TicketAlreadyPurchasedException;
import com.ticketseller.service.tickets.exceptions.TicketNotFoundException;
import com.ticketseller.service.users.User;
import com.ticketseller.service.users.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Optional;

@Primary
@Repository
public class TicketRepositoryImpl implements TicketRepository {

    private final TicketDao ticketDao;
    private final UserDao userDao;
    private final PlatformTransactionManager transactionManager;

    @Autowired
    public TicketRepositoryImpl(TicketDao ticketDao, UserDao userDao, PlatformTransactionManager transactionManager) {
        this.ticketDao = ticketDao;
        this.userDao = userDao;
        this.transactionManager = transactionManager;
    }

    @Override
    public Optional<? extends Ticket> findById(Long id) {
        return ticketDao.findById(id);
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
        return ticketDao.findById(id);
    }

    @Override
    public void create(Ticket user) {
        // Not yet implemented. No requirements to do it.
    }

    @Override
    public void update(Ticket ticket) {
        ticketDao.update(ticket);
    }

    @Override
    public void delete(Ticket ticket) {
        // Not yet implemented. No requirements to do it.
    }

    @Override
    public void purchaseTicket(String login, long ticketId)
            throws UserNotFoundException, TicketNotFoundException, TicketAlreadyPurchasedException {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("Purchase ticket");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            User user = userDao.findByLogin(login).orElseThrow(() -> new UserNotFoundException(login));
            Ticket ticket = ticketDao.findById(ticketId).orElseThrow(() -> new TicketNotFoundException(ticketId));
            if (ticket.getOwnerId() > 0) {
                throw new TicketAlreadyPurchasedException(ticketId);
            }
            ticket.setOwnerId(user.id());
            ticketDao.update(ticket);
        } catch (UserNotFoundException | TicketNotFoundException | TicketAlreadyPurchasedException ex) {
            transactionManager.rollback(status);
            throw ex;
        }
        transactionManager.commit(status);
    }
}
