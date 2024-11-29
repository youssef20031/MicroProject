package com.microproject.microproject.Controller;

// Java
import com.microproject.microproject.model.*;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Initialize Register File
        RegisterFile registerFile = new RegisterFile();

        // Set initial value of R1 to 70
        Register[] integerRegisterFile = registerFile.getIntegerRegisterFile();
        integerRegisterFile[1] = new Register("R1", 70, "");

        // Prepare instructions
        List<Instruction> instructions = new ArrayList<>();
        instructions.add(new Instruction("L.D", 0, "F0", "0", "R1"));     // L.D F0, 0(R1)
        instructions.add(new Instruction("MUL.D", 0, "F4", "F0", "F2"));  // MUL.D F4, F0, F2
        instructions.add(new Instruction("S.D", 0, "F4", "0", "R1"));     // S.D F4, 0(R1)

        // Latency for each operation
        Map<String, Integer> latencies = new HashMap<>();
        latencies.put("L.D", 2);    // Example latency
        latencies.put("MUL.D", 10);
        latencies.put("S.D", 2);

        // Initialize Reservation Stations
        ReservationStation addSubRS = new ReservationStation(3, "Add/Sub");
        ReservationStation mulDivRS = new ReservationStation(2, "Mul/Div");
        ReservationStation loadRS = new ReservationStation(2, "Load");
        ReservationStation storeRS = new ReservationStation(2, "Store");

        // Create a list to hold all reservation stations
        List<ReservationStation> reservationStations = Arrays.asList(
                addSubRS, mulDivRS, loadRS, storeRS
        );

        // Initialize Common Data Bus
        CommonDataBus cdb = new CommonDataBus();

        // Simulation variables
        int cycle = 1;
        int pc = 0;  // Program counter for instructions

        // Main simulation loop
        while (true) {
            System.out.println("Cycle: " + cycle);

            // Write-back stage
            cdb.broadcast();

            // Execute stage
            for (ReservationStation rs : reservationStations) {
                rs.execute(cdb);
            }

            // Issue stage
            if (pc < instructions.size()) {
                Instruction inst = instructions.get(pc);
                boolean issued = issueInstruction(inst, reservationStations, registerFile, latencies);
                if (issued) {
                    pc++;
                }
            }

            // Check if all instructions have been processed
            boolean done = (pc >= instructions.size());
            for (ReservationStation rs : reservationStations) {
                if (!rs.isEmpty()) {
                    done = false;
                    break;
                }
            }
            if (done) {
                break;
            }

            cycle++;
        }

        System.out.println("Simulation completed in " + cycle + " cycles.");
    }

    private static boolean issueInstruction(Instruction inst, List<ReservationStation> reservationStations,
                                            RegisterFile registerFile, Map<String, Integer> latencies) {
        String opcode = inst.getOpcode();
        ReservationStation rs = null;

        switch (opcode) {
            case "ADD.D":
            case "SUB.D":
                rs = reservationStations.stream()
                        .filter(r -> r.getName().equals("Add/Sub"))
                        .findFirst().orElse(null);
                break;
            case "MUL.D":
            case "DIV.D":
                rs = reservationStations.stream()
                        .filter(r -> r.getName().equals("Mul/Div"))
                        .findFirst().orElse(null);
                break;
            case "L.D":
                rs = reservationStations.stream()
                        .filter(r -> r.getName().equals("Load"))
                        .findFirst().orElse(null);
                break;
            case "S.D":
                rs = reservationStations.stream()
                        .filter(r -> r.getName().equals("Store"))
                        .findFirst().orElse(null);
                break;
            default:
                System.out.println("Unsupported opcode: " + opcode);
                return false;
        }

        if (rs != null && !rs.isFull()) {
            rs.issue(inst, registerFile, latencies.get(opcode));
            return true;
        } else {
            return false;  // Reservation station is full or not found
        }
    }
}