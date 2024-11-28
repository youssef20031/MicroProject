package com.microproject.microproject.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InstructionReader {

    public static List<String[]> readInstructions(String filePath) throws IOException {
        List<String[]> instructions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineWords = line.split("\\s+|,\\s*|\\(|\\)");
                List<String> words = new ArrayList<>();
                for (String word : lineWords) {
                    if (!word.isEmpty()) {
                        words.add(word);
                    }
                }
                if (!words.isEmpty()) {
                    instructions.add(words.toArray(new String[0]));
                }
            }
        }
        return instructions;
    }

    public static void main(String[] args) {
        try {
            String filePath = "src/main/java/com/microproject/microproject/util/test.txt"; // Adjust the path as needed
            List<String[]> instructions = readInstructions(filePath);
            for (String[] instruction : instructions) {
                System.out.println(String.join(", ", instruction));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}