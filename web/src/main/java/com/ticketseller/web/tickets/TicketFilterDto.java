package com.ticketseller.web.tickets;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ticketseller.service.tickets.TicketFilter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Schema(description = "Ticket Filter", name = "TicketFilter")
public record TicketFilterDto(
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @JsonProperty LocalDateTime date,
        @RequestParam(required = false)
        @JsonProperty String departurePoint,
        @RequestParam(required = false)
        @JsonProperty String destinationPoint,
        @RequestParam(required = false)
        @JsonProperty String carrierName
        ) implements TicketFilter {
}
