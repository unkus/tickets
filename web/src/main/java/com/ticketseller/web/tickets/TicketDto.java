package com.ticketseller.web.tickets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.ticketseller.service.pathes.Path;
import com.ticketseller.service.tickets.Ticket;
import com.ticketseller.service.tickets.TicketDetails;
import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Timestamp;

@Schema
@JsonRootName("Ticket")
public record TicketDto(
        @JsonProperty Long id,
        @JsonProperty Path path,
        @JsonProperty Integer place,
        @JsonProperty Double price,
        @JsonProperty Timestamp date
) {
    public static TicketDto of(TicketDetails ticket) {
        return new TicketDto(
                ticket.id(),
                ticket.path(),
                ticket.place(),
                ticket.price(),
                ticket.date()
        );
    }

}
