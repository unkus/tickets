package com.ticketseller.databse.users;

import com.ticketseller.service.users.User;
import com.ticketseller.database.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Primary
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserDao userDao;

    @Autowired
    public UserRepositoryImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userDao.findById(id));
    }

    @Override
    public Optional<User> findByLogin(String login)  {
        return Optional.ofNullable(userDao.findByLogin(login));
    }

    @Override
    public void create(User user) {
        userDao.create(user);
    }

    @Override
    public UserEntity read(Long id) {
        return null;
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
