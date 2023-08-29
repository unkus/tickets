package com.ticketseller.service.users;

import com.ticketseller.service.tickets.TicketDetails;
import com.ticketseller.database.tickets.TicketRepository;
import com.ticketseller.database.users.UserRepository;
import com.ticketseller.service.users.exceptions.UserAlreadyRegisteredException;
import com.ticketseller.service.users.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Primary
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final TicketRepository ticketRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, TicketRepository ticketRepository) {
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public User getUser(String login) throws UserNotFoundException {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(login));
    }

    @Override
    public void registerUser(User user) throws UserAlreadyRegisteredException {
        if(userRepository.findByLogin(user.login()).isPresent()) {
            throw new UserAlreadyRegisteredException(user.login());
        }

        userRepository.create(user);
    }

    @Override
    public Page<TicketDetails> getTickets(String login, Pageable pageable) throws UserNotFoundException {
        User user = userRepository.findByLogin(login).orElseThrow(() -> new UserNotFoundException(login));
        return ticketRepository.getPurchasedTickets(user, pageable);
    }
}
