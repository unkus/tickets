package com.ticketseller.service.tickets;

import com.ticketseller.service.pathes.PathDetails;

import java.sql.Timestamp;

public record TicketDetails(
        Long id,
        PathDetails path,
        Integer place,
        Double price,
        Timestamp date
) {
}
