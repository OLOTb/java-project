package com.hotel;

import com.hotel.exception.RoomUnavailableException;
import com.hotel.model.Client;
import com.hotel.model.Reservation;
import com.hotel.model.Room;
import com.hotel.service.PriceCalculator;
import com.hotel.service.ReservationService;
import com.hotel.service.StandardPriceCalculator;
import com.hotel.utils.CsvLoader;
import com.hotel.utils.CustomLogger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Application {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static void main(String[] args) {
        CustomLogger.log("--- Starting Hotel Reservation System ---");

        String filePath = "src/main/resources/rooms.csv";
        List<Room> rooms = CsvLoader.loadRooms(filePath);

        if (rooms.isEmpty()) {
            CustomLogger.log("No rooms loaded. Please check the CSV file.");
            return;
        }

        PriceCalculator priceCalculator = new StandardPriceCalculator();
        ReservationService reservationService = new ReservationService(rooms, priceCalculator);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== HOTEL MENU ===");
            System.out.println("1. View all rooms");
            System.out.println("2. Search available rooms & Book");
            System.out.println("3. View all reservations");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    for (Room room : reservationService.getRooms()) {
                        System.out.println(room.getRoomDescription());
                    }
                    break;
                case "2":
                    handleSearchAndBook(scanner, reservationService);
                    break;
                case "3":
                    List<Reservation> reservations = reservationService.getReservations();
                    if (reservations.isEmpty()) {
                        System.out.println("No reservations found.");
                    } else {
                        for (Reservation res : reservations) {
                            System.out.println("Room " + res.getRoom().getNumber() + " booked by " + res.getClient().getFirstName() + " " + res.getClient().getLastName() + " from " + res.getCheckIn().format(DATE_FORMATTER) + " to " + res.getCheckOut().format(DATE_FORMATTER));
                        }
                    }
                    break;
                case "4":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please choose between 1 and 4.");
            }
        }

        scanner.close();
        CustomLogger.log("--- Shutting down ---");
    }

    private static void handleSearchAndBook(Scanner scanner, ReservationService reservationService) {
        try {
            System.out.print("Enter check-in date (DD-MM-YYYY): ");
            LocalDate checkIn = LocalDate.parse(scanner.nextLine().trim(), DATE_FORMATTER);

            System.out.print("Enter check-out date (DD-MM-YYYY): ");
            LocalDate checkOut = LocalDate.parse(scanner.nextLine().trim(), DATE_FORMATTER);

            System.out.print("Enter number of guests: ");
            int guests = Integer.parseInt(scanner.nextLine().trim());

            List<Room> availableRooms = reservationService.findAvailableRooms(checkIn, checkOut, guests);

            if (availableRooms.isEmpty()) {
                CustomLogger.log("No rooms available for these dates and capacity.");
                return;
            }

            System.out.println("\nAvailable rooms:");
            for (Room room : availableRooms) {
                System.out.println(room.getRoomDescription());
            }

            System.out.print("\nDo you want to book a room? (yes/no): ");
            String answer = scanner.nextLine().trim();
            if (!answer.equalsIgnoreCase("yes") && !answer.equalsIgnoreCase("y")) {
                return;
            }

            System.out.print("Enter the room number you want to book: ");
            int roomNumber = Integer.parseInt(scanner.nextLine().trim());

            Optional<Room> selectedRoom = reservationService.findRoomByNumber(roomNumber);
            if (selectedRoom.isEmpty()) {
                CustomLogger.log("Room number " + roomNumber + " does not exist.");
                return;
            }

            System.out.print("Enter your first name: ");
            String firstName = scanner.nextLine().trim();

            System.out.print("Enter your last name: ");
            String lastName = scanner.nextLine().trim();

            System.out.print("Enter your email: ");
            String email = scanner.nextLine().trim();

            Client client = new Client(firstName, lastName, email);
            reservationService.bookRoom(selectedRoom.get(), client, guests, checkIn, checkOut);

        } catch (DateTimeParseException e) {
            CustomLogger.log("Invalid date format. Please use DD-MM-YYYY.");
        } catch (NumberFormatException e) {
            CustomLogger.log("Invalid number entered.");
        } catch (IllegalArgumentException | RoomUnavailableException e) {
            CustomLogger.log("Booking error: " + e.getMessage());
        }
    }
}