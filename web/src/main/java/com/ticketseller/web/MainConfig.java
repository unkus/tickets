package com.ticketseller.web;

import com.ticketseller.service.tickets.TicketDetails;
import com.ticketseller.web.tickets.TicketController;
import com.ticketseller.web.tickets.TicketDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Configuration
public class MainConfig {

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
