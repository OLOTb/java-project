package com.hotel.service;

import com.hotel.exception.RoomUnavailableException;
import com.hotel.model.Client;
import com.hotel.model.Reservation;
import com.hotel.model.Room;
import com.hotel.utils.CustomLogger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReservationService {
    private final List<Room> rooms;
    private final List<Reservation> reservations;
    private final PriceCalculator priceCalculator;

    public ReservationService(List<Room> rooms, PriceCalculator priceCalculator) {
        this.rooms = new ArrayList<>(rooms);
        this.reservations = new ArrayList<>();
        this.priceCalculator = priceCalculator;
    }

    public Optional<Room> findRoomByNumber(int roomNumber) {
        return rooms.stream()
                .filter(room -> room.getNumber() == roomNumber)
                .findFirst();
    }

    public List<Room> findAvailableRooms(LocalDate checkIn, LocalDate checkOut, int guests) {
        if (checkIn == null || checkOut == null || !checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date.");
        }
        if (guests <= 0) {
            throw new IllegalArgumentException("Number of guests must be greater than zero.");
        }

        return rooms.stream()
                .filter(room -> room.getCapacity() >= guests)
                .filter(room -> isRoomAvailable(room, checkIn, checkOut))
                .collect(Collectors.toList());
    }

    public Reservation bookRoom(Room room, Client client, int guests, LocalDate checkIn, LocalDate checkOut) throws RoomUnavailableException {
        if (room == null || client == null || checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("Booking parameters cannot be null.");
        }
        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date.");
        }
        if (guests <= 0 || guests > room.getCapacity()) {
            throw new IllegalArgumentException("Invalid number of guests for this room capacity.");
        }
        if (!isRoomAvailable(room, checkIn, checkOut)) {
            throw new RoomUnavailableException("Room " + room.getNumber() + " is not available for these dates.");
        }

        Reservation reservation = new Reservation(room, client, checkIn, checkOut);
        reservations.add(reservation);

        double totalPrice = priceCalculator.calculateTotalPrice(room, checkIn, checkOut);
        CustomLogger.log("Reservation confirmed for " + client.getFirstName() + " in Room " + room.getNumber() + ". Total price: $" + totalPrice);

        return reservation;
    }

    public boolean isRoomAvailable(Room room, LocalDate checkIn, LocalDate checkOut) {
        return reservations.stream()
                .filter(res -> res.getRoom().getNumber() == room.getNumber())
                .noneMatch(res -> checkIn.isBefore(res.getCheckOut()) && checkOut.isAfter(res.getCheckIn()));
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }
}