package com.ticketseller.service.pathes;

import com.ticketseller.service.carriers.CarrierDetails;

import java.sql.Time;

public record PathDetails(
        String departurePoint,
        String destinationPoint,
        CarrierDetails carrier,
        Time duration
) implements Path {
}
