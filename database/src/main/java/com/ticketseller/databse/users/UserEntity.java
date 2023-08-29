package com.ticketseller.databse.users;

import com.ticketseller.service.users.User;

public record UserEntity(
        Long id,
        String login,
        String password,
        String name,
        String surname,
        String patronymic
) implements User {
}
