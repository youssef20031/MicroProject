package com.microproject.microproject.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InstructionReader {

    /**
     * Reads instructions from the specified file.
     * 
     * For 'Load' and 'Store' instructions, if the operand count is 3, a "0" is appended
     * to ensure consistent operand count.
     * 
     * @param filePath Path to the instruction file.
     * @return List of instruction arrays.
     * @throws IOException If an I/O error occurs reading from the file.
     */
    public static List<String[]> readInstructions(String filePath) throws IOException {
        List<String[]> instructions = new ArrayList<>();
        
        // Define opcodes that are considered as 'Load' or 'Store' instructions
        List<String> loadStoreOpcodes = List.of("L.D", "S.D", "LD", "SD", "SW", "S.S", "LW", "L.S"); // Add more as needed

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line based on whitespace, commas, or parentheses
                String[] lineWords = line.split("\\s+|,\\s*|\\(|\\)");
                List<String> words = new ArrayList<>();
                for (String word : lineWords) {
                    if (!word.isEmpty()) {
                        words.add(word);
                    }
                }
                if (!words.isEmpty()) {
                    String opcode = words.get(0);
                    
                    // Condition 1: If the instruction has only two elements, append "0"
                    if (words.size() == 2) {
                        words.add("0");
                    }
                    
                    // Condition 2: If the opcode is 'Load' or 'Store' and there are three elements, append "0"
                    if (loadStoreOpcodes.contains(opcode) && words.size() == 3) {
                        words.add("0");
                    }
                    
                    instructions.add(words.toArray(new String[0]));
                }
            }
        }
        return instructions;
    }

    public static void main(String[] args) {
        try {
            String filePath = "src/main/java/com/microproject/microproject/text/instruction.txt"; // Adjust the path as needed
            List<String[]> instructions = readInstructions(filePath);
            for (String[] instruction : instructions) {
                System.out.println(String.join(", ", instruction));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}