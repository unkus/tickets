package com.ticketseller.databse.tickets;

import com.ticketseller.service.tickets.Ticket;

import java.sql.Timestamp;

public class TicketEntity implements Ticket {

    private Long id;
    private Integer pathId;
    private Integer place;
    private Double price;
    private Timestamp date;
    private Long ownerId;

    public TicketEntity(Long id, Integer pathId, Integer place, Double price, Timestamp date, Long ownerId) {
        this.id = id;
        this.pathId = pathId;
        this.place = place;
        this.price = price;
        this.date = date;
        this.ownerId = ownerId;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Integer getPathId() {
        return pathId;
    }

    public void setPathId(Integer pathId) {
        this.pathId = pathId;
    }

    @Override
    public Integer getPlace() {
        return place;
    }

    @Override
    public void setPlace(Integer place) {
        this.place = place;
    }

    @Override
    public Double getPrice() {
        return price;
    }

    @Override
    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public Timestamp getDate() {
        return date;
    }

    @Override
    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Override
    public Long getOwnerId() {
        return ownerId;
    }

    @Override
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
