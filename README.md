# Hotel Reservation System

## 1. Purpose of the Application
The Hotel Reservation System is a console-based Java application designed to manage hotel room bookings. It loads initial room data from a CSV file, allows users to view all rooms, search for available rooms based on check-in/check-out dates and guest capacity, book rooms, and view existing reservations while preventing double-bookings.

## 2. Main Features
- **Automatic Data Initialization:** Loads 13 rooms of different categories from `src/main/resources/rooms.csv` at startup.
- **Interactive Console Menu:** Allows users to navigate options using standard input (`Scanner`).
- **Room Search & Filtering:** Filters available rooms by guest capacity and date availability (`DD-MM-YYYY` format).
- **Conflict Detection:** Prevents overlapping reservations for the same room and validates guest counts and date coherence.
- **Price Calculation:** Automatically computes the total stay cost based on the number of nights and room rate.
- **Custom Logging:** Prints application logs twice to the console via `CustomLogger`.

## 3. Main Classes & Project Structure
- `com.hotel.Application`: Main entry point handling user interaction and menu navigation.
- `com.hotel.model.Room`: Abstract base class representing a generic hotel room.
- `com.hotel.model.Single`, `Double`, `Family`, `Suite`: Concrete subclasses of `Room`.
- `com.hotel.model.Client`: Represents a customer (first name, last name, email).
- `com.hotel.model.Reservation`: Links a `Room`, a `Client`, a check-in `LocalDate`, and a check-out `LocalDate`.
- `com.hotel.service.PriceCalculator`: Interface defining the contract for price calculation.
- `com.hotel.service.StandardPriceCalculator`: Concrete implementation multiplying the number of nights by the room's nightly rate.
- `com.hotel.service.ReservationService`: Core business logic for searching rooms, checking date overlaps, and creating reservations.
- `com.hotel.exception.RoomUnavailableException`: Custom checked exception thrown when a room is already booked for the requested dates.
- `com.hotel.utils.CsvLoader`: Utility class that reads and parses `rooms.csv`.
- `com.hotel.utils.CustomLogger`: Utility class handling console logging.

## 4. OOP Concepts Used
- **Inheritance:** `Single`, `Double`, `Family`, and `Suite` extend the abstract class `Room` (`extends Room`), inheriting common fields (`number`, `capacity`, `price`) and getters.
- **Polymorphism:** The application stores and manipulates all room subtypes uniformly inside a `List<Room>`. Calling `room.getRoomDescription()` dynamically executes the overridden implementation of the specific room subclass (`Single`, `Double`, `Family`, or `Suite`). Similarly, `ReservationService` calls `priceCalculator.calculateTotalPrice(...)` through the `PriceCalculator` interface.
- **Abstract Class & Interface:**
    - **Abstract Class:** `Room` is declared `abstract` because a generic room cannot be instantiated without a specific category, and it enforces the implementation of `getRoomDescription()`.
    - **Interface:** `PriceCalculator` defines the pricing strategy contract, implemented by `StandardPriceCalculator`.
- **Custom Exception:** `RoomUnavailableException` (in `com.hotel.exception`) is thrown by `ReservationService.bookRoom()` when a date conflict is detected, and caught in `Application` to display a clear error message.

## 5. Technical Decisions
1. **Using `java.time.LocalDate` and `DateTimeFormatter`:** Chosen over legacy `java.util.Date` for immutability, thread-safety, and built-in date arithmetic (`isBefore`, `isAfter`, `ChronoUnit.DAYS.between`), formatted with `dd-MM-yyyy` for user-friendly console input.
2. **Java Stream API & `Optional`:** Used in `ReservationService` (`findRoomByNumber`, `findAvailableRooms`, `isRoomAvailable`) to write declarative, readable filtering logic and avoid returning `null` references.
3. **Strategy Pattern via `PriceCalculator`:** Decoupled pricing calculation from `ReservationService` so new pricing rules (e.g., seasonal discounts or loyalty rates) can be added without modifying the booking logic.
4. **Defensive CSV Parsing with `java.nio.file.Files`:** Built `CsvLoader` to strip quotes, ignore blank lines, and support both `;` and `,` delimiters while catching `IOException` and `NumberFormatException`.

## 6. How Data is Loaded
When `Application.main()` starts, it calls `CsvLoader.loadRooms("src/main/resources/rooms.csv")`. `CsvLoader` uses `java.nio.file.Path` and `Files.readAllLines()` (UTF-8) to read the file, skips the header row, splits each line into fields, parses numeric values, and instantiates the corresponding `Room` subclass (`Single`, `Double`, `Family`, or `Suite`).

## 7. SOLID Principles Applied
- **Single Responsibility Principle (SRP):** Responsibilities are strictly separated: `CsvLoader` only parses files, `ReservationService` only handles booking rules, `StandardPriceCalculator` only computes prices, and `Application` only handles console I/O.
- **Open/Closed Principle (OCP):** New room types can be added by extending `Room`, and new pricing rules can be added by implementing `PriceCalculator` without altering `ReservationService`.
- **Liskov Substitution Principle (LSP):** Any subclass of `Room` (`Single`, `Double`, `Family`, `Suite`) can replace a `Room` reference in `ReservationService` or `Application` without breaking behavior.
- **Interface Segregation Principle (ISP):** `PriceCalculator` is a focused single-method interface (`calculateTotalPrice`), ensuring implementing classes only implement what they need.
- **Dependency Inversion Principle (DIP):** `ReservationService` depends on the `PriceCalculator` abstraction rather than the concrete `StandardPriceCalculator`, injected via its constructor.

## 8. How to Run the Application
### From IntelliJ IDEA
1. Open the project in IntelliJ IDEA as a Maven project.
2. Navigate to `src/main/java/com/hotel/Application.java`.
3. Right-click the file and select **Run 'Application.main()'**.

### From the Command Line
```bash
mvn compile exec:java -Dexec.mainClass="com.hotel.Application"