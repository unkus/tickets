package com.ticketseller.web;

import com.ticketseller.service.tickets.TicketDetails;
import com.ticketseller.web.tickets.TicketController;
import com.ticketseller.web.tickets.TicketDto;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@SpringBootApplication(scanBasePackages = "com.ticketseller")
@OpenAPIDefinition(info = @Info(title = "Ticket Seller API", version = "0.1", description = "Demo application to sell tickets"))
public class TicketApp {
    public static void main(String[] args) {
        SpringApplication.run(TicketApp.class, args);
    }

    @Bean
    public RepresentationModelAssembler<TicketDetails, EntityModel<TicketDto>> ticketModelAssembler() {
        return entity -> EntityModel.of(
                TicketDto.of(entity),
                linkTo(methodOn(TicketController.class).one(entity.id())).withSelfRel(),
                linkTo(methodOn(TicketController.class).availableTickets(
                        Pageable.unpaged(),
                        null)
                ).withRel("all"));
    }
}
