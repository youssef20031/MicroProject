package com.microproject.microproject.util;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to read reservation station configurations from a text file.
 */
public class ReservationStationReader {

    /**
     * Reads reservation stations from the specified file.
     *
     * @param filePath Path to the reservation stations configuration file.
     * @return List of Pairs where the first element is the capacity (Integer)
     *         and the second element is the reservation station name (String).
     * @throws IOException If an I/O error occurs reading from the file.
     */
    public static List<Pair<Integer, String>> readReservationStations(String filePath) throws IOException {
        List<Pair<Integer, String>> reservationStations = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                
                // Skip empty lines
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",", 2); // Split into two parts: capacity and name

                if (parts.length != 2) {
                    System.err.println("Invalid format in reservation stations file: " + line);
                    continue; // Skip invalid lines
                }

                try {
                    int capacity = Integer.parseInt(parts[0].trim());
                    String stationName = parts[1].trim();
                    reservationStations.add(new Pair<>(capacity, stationName));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number format in reservation stations file: " + line);
                    // Optionally, you can throw the exception or continue
                }
            }
        }

        return reservationStations;
    }
}