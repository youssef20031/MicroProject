package com.microproject.microproject.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InstructionLatencyReader {

    public static List<String> readInstructionsAndLatencies(String filePath) throws IOException {
        List<String> instructionsAndLatencies = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineWords = line.split("\\s+");
                for (String word : lineWords) {
                    instructionsAndLatencies.add(word);
                }
            }
        }
        return instructionsAndLatencies;
    }

    public static void main(String[] args) {
        try {
            String filePath = "src/main/java/com/microproject/microproject/util/test2.txt"; // Adjust the path as needed
            List<String> instructionsAndLatencies = readInstructionsAndLatencies(filePath);
            for (String item : instructionsAndLatencies) {
                System.out.println(item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}