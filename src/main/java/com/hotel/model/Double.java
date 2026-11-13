package com.hotel.model;

public class Double extends Room {

    public Double(int number, int capacity, double price) {
        super(number, capacity, price);
    }

    @Override
    public String getRoomDescription() {
        return "Double Room " + getNumber() + " (Capacity: " + getCapacity() + ", Price: $" + getPrice() + ")";
    }
}