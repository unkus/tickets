package com.ticketseller.service.users.exceptions;

public class UserAlreadyRegisteredException extends Exception {

    public UserAlreadyRegisteredException(String login) {
        super("User %s already registered".formatted(login));
    }

}
