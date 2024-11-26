package com.microproject.microproject.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InstructionReader {

    public static String[] readInstructions(String filePath) throws IOException {
        List<String> words = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineWords = line.split("\\s+|,\\s*|\\(|\\)");
                for (String word : lineWords) {
                    if (!word.isEmpty()) {
                        words.add(word);
                    }
                }
            }
        }
        return words.toArray(new String[0]);
    }

    public static void main(String[] args) {
        try {
            String filePath = "src/main/java/com/microproject/microproject/util/test.txt"; // Adjust the path as needed
            String[] instructions = readInstructions(filePath);
            for (String word : instructions) {
                System.out.println(word);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}