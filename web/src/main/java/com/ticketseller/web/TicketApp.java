package com.ticketseller.web;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.ticketseller")
@OpenAPIDefinition(info = @Info(title = "Ticket Seller API", version = "0.1", description = "Demo application to sell tickets"))
public class TicketApp {
    public static void main(String[] args) {
        SpringApplication.run(TicketApp.class, args);
    }

}
