package com.microproject.microproject.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

public class InstructionLatencyReader {

    public static List<Pair<String, Integer>> readInstructionsAndLatencies(String filePath) throws IOException {
        List<Pair<String, Integer>> instructionsAndLatencies = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineWords = line.split("\\s+");
                if (lineWords.length == 2) {
                    String instruction = lineWords[0];
                    int latency = Integer.parseInt(lineWords[1]);
                    instructionsAndLatencies.add(new Pair<>(instruction, latency));
                }
            }
        }
        return instructionsAndLatencies;
    }

    public static void main(String[] args) {
        try {
            String filePath = "src/main/java/com/microproject/microproject/util/test2.txt"; // Adjust the path as needed
            List<Pair<String, Integer>> instructionsAndLatencies = readInstructionsAndLatencies(filePath);
            for (Pair<String, Integer> item : instructionsAndLatencies) {
                System.out.println(item.getKey() + ": " + item.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}