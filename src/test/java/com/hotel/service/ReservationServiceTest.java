package com.hotel.service;

import com.hotel.exception.RoomUnavailableException;
import com.hotel.model.Client;
import com.hotel.model.Double;
import com.hotel.model.Reservation;
import com.hotel.model.Room;
import com.hotel.model.Single;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    private ReservationService reservationService;
    private PriceCalculator priceCalculator;
    private Room singleRoom;
    private Room doubleRoom;
    private Client client;

    @BeforeEach
    void setUp() {
        priceCalculator = new StandardPriceCalculator();
        singleRoom = new Single(101, 1, 70.0);
        doubleRoom = new Double(104, 2, 85.0);

        List<Room> rooms = new ArrayList<>();
        rooms.add(singleRoom);
        rooms.add(doubleRoom);

        reservationService = new ReservationService(rooms, priceCalculator);
        client = new Client("John", "Doe", "john@email.com");
    }

    @Test
    void shouldBookAvailableRoomSuccessfully() throws RoomUnavailableException {
        LocalDate checkIn = LocalDate.of(2026, 12, 1);
        LocalDate checkOut = LocalDate.of(2026, 12, 5);

        Reservation reservation = reservationService.bookRoom(singleRoom, client, 1, checkIn, checkOut);

        assertNotNull(reservation);
        assertEquals(1, reservationService.getReservations().size());
        assertEquals(101, reservation.getRoom().getNumber());
    }

    @Test
    void shouldCalculateCorrectTotalPrice() {
        LocalDate checkIn = LocalDate.of(2026, 12, 1);
        LocalDate checkOut = LocalDate.of(2026, 12, 5);

        double total = priceCalculator.calculateTotalPrice(singleRoom, checkIn, checkOut);

        assertEquals(280.0, total);
    }

    @Test
    void shouldAllowBackToBackReservationsOnSameCheckOutAndCheckInDay() throws RoomUnavailableException {
        LocalDate checkIn1 = LocalDate.of(2026, 12, 1);
        LocalDate checkOut1 = LocalDate.of(2026, 12, 5);
        LocalDate checkIn2 = LocalDate.of(2026, 12, 5);
        LocalDate checkOut2 = LocalDate.of(2026, 12, 8);

        reservationService.bookRoom(singleRoom, client, 1, checkIn1, checkOut1);
        Reservation secondReservation = reservationService.bookRoom(singleRoom, client, 1, checkIn2, checkOut2);

        assertNotNull(secondReservation);
        assertEquals(2, reservationService.getReservations().size());
    }

    @Test
    void shouldFilterAvailableRoomsByCapacityAndDates() {
        LocalDate checkIn = LocalDate.of(2026, 12, 1);
        LocalDate checkOut = LocalDate.of(2026, 12, 5);

        List<Room> availableForTwo = reservationService.findAvailableRooms(checkIn, checkOut, 2);

        assertEquals(1, availableForTwo.size());
        assertEquals(104, availableForTwo.get(0).getNumber());
    }

    @Test
    void shouldThrowExceptionWhenRoomIsAlreadyBookedForOverlappingDates() throws RoomUnavailableException {
        LocalDate checkIn1 = LocalDate.of(2026, 12, 1);
        LocalDate checkOut1 = LocalDate.of(2026, 12, 5);
        reservationService.bookRoom(singleRoom, client, 1, checkIn1, checkOut1);

        LocalDate overlappingCheckIn = LocalDate.of(2026, 12, 3);
        LocalDate overlappingCheckOut = LocalDate.of(2026, 12, 7);

        assertThrows(RoomUnavailableException.class, () ->
                reservationService.bookRoom(singleRoom, client, 1, overlappingCheckIn, overlappingCheckOut)
        );
        assertEquals(1, reservationService.getReservations().size());
    }

    @Test
    void shouldThrowExceptionWhenGuestsExceedRoomCapacity() {
        LocalDate checkIn = LocalDate.of(2026, 12, 1);
        LocalDate checkOut = LocalDate.of(2026, 12, 5);

        assertThrows(IllegalArgumentException.class, () ->
                reservationService.bookRoom(singleRoom, client, 2, checkIn, checkOut)
        );
    }

    @Test
    void shouldThrowExceptionWhenCheckOutDateIsBeforeOrEqualCheckInDate() {
        LocalDate checkIn = LocalDate.of(2026, 12, 5);
        LocalDate invalidCheckOut = LocalDate.of(2026, 12, 1);

        assertThrows(IllegalArgumentException.class, () ->
                reservationService.bookRoom(singleRoom, client, 1, checkIn, invalidCheckOut)
        );
    }
}