package com.ticketseller.service.users;

public interface User {
    Long id();
    String login();
    String password();
    String name();
    String surname();
    String patronymic();
}
