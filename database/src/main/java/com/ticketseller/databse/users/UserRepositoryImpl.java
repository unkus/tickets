package com.ticketseller.databse.users;

import com.ticketseller.service.users.User;
import com.ticketseller.database.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;

@Primary
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserDao userDao;
    private final TransactionTemplate transactionTemplate;

    @Autowired
    public UserRepositoryImpl(UserDao userDao, TransactionTemplate transactionTemplate) {
        this.userDao = userDao;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public void create(User user) {
        // Stub to future functional
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userDao.findById(id));
    }

    @Override
    public Optional<User> findByLogin(String login)  {
        return userDao.findByLogin(login);
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public void delete(User user) {
        userDao.delete(user.id());
    }
}
