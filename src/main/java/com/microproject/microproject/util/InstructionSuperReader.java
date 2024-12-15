package com.microproject.microproject.util;

import com.microproject.microproject.model.Instruction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InstructionSuperReader {

    public static String[][] readInstructionsAndLatencies(String instructionsFilePath, String latenciesFilePath) throws IOException {
        List<String[]> instructionsData = InstructionReader.readInstructions(instructionsFilePath);
        List<String[]> latenciesData = InstructionReader.readInstructions(latenciesFilePath);

        List<String[]> instructions = new ArrayList<>();
        for (String[] instructionData : instructionsData) {
            if (instructionData.length > 0) {
                String opcode = instructionData[0];
                String destination = instructionData.length > 1 ? instructionData[1] : "";
                String source1 = instructionData.length > 2 ? instructionData[2] : "";
                String source2 = instructionData.length > 3 ? instructionData[3] : "";

                String latency = "1"; // Default latency
                for (String[] latencyData : latenciesData) {
                    if (latencyData.length > 1 && latencyData[0].equals(opcode)) {
                        latency = latencyData[1];
                        break;
                    }
                }

                String[] instruction = new String[]{opcode, destination, source1, source2, latency};
                instructions.add(instruction);
            }
        }

        return instructions.toArray(new String[0][]);
    }

    public static void main(String[] args) {
        try {
            String instructionsFilePath = "src/main/java/com/microproject/microproject/text/instruction.txt";
            String latenciesFilePath = "src/main/java/com/microproject/microproject/text/latency.txt"; // Adjust the path as needed

            String[][] instructions = readInstructionsAndLatencies(instructionsFilePath, latenciesFilePath);

            // Print instructions
            for (String[] instruction : instructions) {
                System.out.println("Instruction:" + instruction[0] + " Destination:" + instruction[1] + " Source1:" + instruction[2] + " Source2:" + instruction[3] + " Latency:" + instruction[4]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}