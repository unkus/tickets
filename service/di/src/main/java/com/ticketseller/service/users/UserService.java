package com.ticketseller.service.users;

import com.ticketseller.service.tickets.TicketDetails;
import com.ticketseller.service.users.exceptions.UserAlreadyRegisteredException;
import com.ticketseller.service.users.exceptions.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User getUser(String login) throws UserNotFoundException;

    void registerUser(User user) throws UserAlreadyRegisteredException;

    Page<TicketDetails> getTickets(String login, Pageable pageable) throws UserNotFoundException;
}
