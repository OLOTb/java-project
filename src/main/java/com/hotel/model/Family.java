package com.hotel.model;

public class Family extends Room {

    public Family(int number, int capacity, double price) {
        super(number, capacity, price);
    }

    @Override
    public String getRoomDescription() {
        return "Family Room " + getNumber() + " (Capacity: " + getCapacity() + ", Price: $" + getPrice() + ")";
    }
}