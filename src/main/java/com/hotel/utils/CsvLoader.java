package com.hotel.utils;

import com.hotel.model.*;
import com.hotel.model.Double;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CsvLoader {

    public static List<Room> loadRooms(String filePath) {
        List<Room> rooms = new ArrayList<>();
        Path path = Path.of(filePath);

        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i).replace("\"", "").trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] fields = line.contains(";") ? line.split(";") : line.split(",");

                if (fields.length < 4) {
                    continue;
                }

                int number = Integer.parseInt(fields[0].trim());
                String type = fields[1].trim();
                int capacity = Integer.parseInt(fields[2].trim());
                double price = java.lang.Double.parseDouble(fields[3].trim());

                switch (type.toLowerCase()) {
                    case "single":
                        rooms.add(new Single(number, capacity, price));
                        break;
                    case "double":
                        rooms.add(new Double(number, capacity, price));
                        break;
                    case "family":
                        rooms.add(new Family(number, capacity, price));
                        break;
                    case "suite":
                        rooms.add(new Suite(number, capacity, price));
                        break;
                    default:
                        CustomLogger.log("Unknown room type: " + type);
                }
            }
        } catch (IOException e) {
            CustomLogger.log("Unable to read file: " + e.getMessage());
        } catch (NumberFormatException e) {
            CustomLogger.log("Data format error in CSV: " + e.getMessage());
        }

        return rooms;
    }
}