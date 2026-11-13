package com.hotel.service;

import com.hotel.model.Room;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class StandardPriceCalculator implements PriceCalculator {

    @Override
    public double calculateTotalPrice(Room room, LocalDate checkIn, LocalDate checkOut) {
        long days = ChronoUnit.DAYS.between(checkIn, checkOut);
        return days * room.getPrice();
    }
}