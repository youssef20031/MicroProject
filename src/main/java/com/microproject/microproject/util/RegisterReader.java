package com.microproject.microproject.util;

import javafx.util.Pair;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to read register configurations from a text file.
 */
public class RegisterReader {

    /**
     * Reads registers from the specified file.
     *
     * Each line in the file should be formatted as "RegisterName, Value" (e.g., "F0, 5").
     *
     * @param filePath Path to the register configuration file.
     * @return List of Pairs where the first element is the register name (String)
     *         and the second element is the register value (Integer).
     * @throws IOException If an I/O error occurs reading from the file.
     */
    public static List<Pair<String, Integer>> readRegisters(String filePath) throws IOException {
        List<Pair<String, Integer>> registers = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                // Skip empty lines
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");

                if (parts.length != 2) {
                    System.err.println("Invalid register format: " + line);
                    continue; // Skip invalid lines
                }

                String registerName = parts[0].trim();
                String valueStr = parts[1].trim();

                try {
                    int value = Integer.parseInt(valueStr);
                    registers.add(new Pair<>(registerName, value));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number format for register value: " + line);
                    // Optionally, handle the error or continue
                }
            }
        }

        return registers;
    }

    /**
     * Example main method to demonstrate reading registers.
     */
    public static void main(String[] args) {
        String filePath = "src/main/java/com/microproject/microproject/text/register.txt"; // Adjust the path as needed

        try {
            List<Pair<String, Integer>> registers = readRegisters(filePath);
            for (Pair<String, Integer> register : registers) {
                System.out.println("Register: " + register.getKey() + ", Value: " + register.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}