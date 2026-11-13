package com.hotel.model;

import java.time.LocalDate;

public class Reservation {
    private final Room room;
    private final Client client;
    private final LocalDate checkIn;
    private final LocalDate checkOut;

    public Reservation(Room room, Client client, LocalDate checkIn, LocalDate checkOut) {
        this.room = room;
        this.client = client;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public Room getRoom() {
        return room;
    }

    public Client getClient() {
        return client;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }
}