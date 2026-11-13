package com.hotel.service;

import com.hotel.model.Room;
import java.time.LocalDate;

public interface PriceCalculator {
    double calculateTotalPrice(Room room, LocalDate checkIn, LocalDate checkOut);
}