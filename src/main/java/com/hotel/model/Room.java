package com.hotel.model;

public abstract class Room {
    private final int number;
    private final int capacity;
    private final double price;

    public Room(int number, int capacity, double price) {
        this.number = number;
        this.capacity = capacity;
        this.price = price;
    }

    public int getNumber() {
        return number;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getPrice() {
        return price;
    }

    public abstract String getRoomDescription();
}