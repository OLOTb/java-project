package com.hotel.model;

public class Suite extends Room {

    public Suite(int number, int capacity, double price) {
        super(number, capacity, price);
    }

    @Override
    public String getRoomDescription() {
        return "Suite " + getNumber() + " (Capacity: " + getCapacity() + ", Price: $" + getPrice() + ")";
    }
}