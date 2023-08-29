package com.ticketseller.database.users;

import com.ticketseller.database.CrudRepository;
import com.ticketseller.service.users.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByLogin(String login);

}
