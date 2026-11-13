package com.hotel.model;

public class Single extends Room {

    public Single(int number, int capacity, double price) {
        super(number, capacity, price);
    }

    @Override
    public String getRoomDescription() {
        return "Single Room " + getNumber() + " (Capacity: " + getCapacity() + ", Price: $" + getPrice() + ")";
    }
}