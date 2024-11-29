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
        for (String[] instructionData : instructionsData) {
            if (instructionData.length > 0) {
                String opcode = instructionData[0];
                String destination = instructionData.length > 1 ? instructionData[1] : null;
                String source1 = instructionData.length > 2 ? instructionData[2] : null;
                String source2 = instructionData.length > 3 ? instructionData[3] : null;

                int latency = 1; // Default latency
                for (String[] latencyData : latenciesData) {
                    if (latencyData.length > 1 && latencyData[0].equals(opcode)) {
                        latency = Integer.parseInt(latencyData[1]);
                        break;
                    }
                }

                Instruction instruction = new Instruction(opcode, latency, destination, source1, source2);
                instructions.add(instruction);
            }
        }
        return instructions;
    }

    public static void main(String[] args) {
        try {
            String instructionsFilePath = "C:\\Users\\Yusuf\\Documents\\Developer\\MP\\MicroProject\\src\\main\\java\\com\\microproject\\microproject\\util\\test.txt";
            String latenciesFilePath = "C:\\Users\\Yusuf\\Documents\\Developer\\MP\\MicroProject\\src\\main\\java\\com\\microproject\\microproject\\util\\test2.txt"; // Adjust the path as needed

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