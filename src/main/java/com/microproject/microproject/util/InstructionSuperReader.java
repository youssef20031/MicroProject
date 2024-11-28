package com.microproject.microproject.util;

import com.microproject.microproject.model.Instruction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InstructionSuperReader {

    public static List<Instruction> readInstructionsAndLatencies(String instructionsFilePath, String latenciesFilePath) throws IOException {
        List<String[]> instructionsData = InstructionReader.readInstructions(instructionsFilePath);
        List<String[]> latenciesData = InstructionReader.readInstructions(latenciesFilePath);

        List<Instruction> instructions = new ArrayList<>();
        int minSize = Math.min(instructionsData.size(), latenciesData.size());
        for (int i = 0; i < minSize; i++) {
            String[] instructionData = instructionsData.get(i);
            String[] latencyData = latenciesData.get(i);

            if (instructionData.length > 0 && latencyData.length > 1) {
                String opcode = instructionData[0];
                int latency = Integer.parseInt(latencyData[1]);
                String destination = instructionData.length > 1 ? instructionData[1] : null;
                String source1 = instructionData.length > 2 ? instructionData[2] : null;
                String source2 = instructionData.length > 3 ? instructionData[3] : null;

                Instruction instruction = new Instruction(opcode, latency, destination, source1, source2);
                instructions.add(instruction);
            }
        }
        return instructions;
    }

    public static void main(String[] args) {
        try {
            String instructionsFilePath = "src/main/java/com/microproject/microproject/util/test.txt"; // Adjust the path as needed
            String latenciesFilePath = "src/main/java/com/microproject/microproject/util/test2.txt"; // Adjust the path as needed

            List<Instruction> instructions = readInstructionsAndLatencies(instructionsFilePath, latenciesFilePath);

            // Print instructions
            for (Instruction instruction : instructions) {
                System.out.println(instruction.getOpcode() + " " + instruction.getDestination() + ", " + instruction.getSource1() + ", " + instruction.getSource2() + " (Latency: " + instruction.getLatency() + ")");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}