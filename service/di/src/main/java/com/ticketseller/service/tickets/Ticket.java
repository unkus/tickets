package com.ticketseller.service.tickets;

import java.sql.Timestamp;

public interface Ticket {
    Long getId();
    void setId(Long id);

    Integer getPathId();
    void setPathId(Integer pathId);

    Integer getPlace();
    void setPlace(Integer place);

    Double getPrice();
    void setPrice(Double price);

    Timestamp getDate();
    void setDate(Timestamp date);

    Long getOwnerId();
    void setOwnerId(Long ownerId);
}
