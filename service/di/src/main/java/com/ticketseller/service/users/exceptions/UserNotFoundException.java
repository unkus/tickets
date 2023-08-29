package com.ticketseller.service.users.exceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(String login) {
        super("User %s not found".formatted(login));
    }

}
