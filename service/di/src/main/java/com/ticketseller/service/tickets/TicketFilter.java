package com.ticketseller.service.tickets;

import java.time.LocalDateTime;

public interface TicketFilter {
    LocalDateTime date();

    String departurePoint();

    String destinationPoint();

    String carrierName();
}
