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
        latencies.put("L.D", 2);
        latencies.put("MUL.D", 10);
        latencies.put("S.D", 2);

        // Initialize Reservation Stations
        ReservationStation addSubRS = new ReservationStation(3, "Add/Sub");
        ReservationStation mulDivRS = new ReservationStation(2, "Mul/Div");
        ReservationStation loadRS = new ReservationStation(2, "Load");
        ReservationStation storeRS = new ReservationStation(2, "Store");

        // List of reservation stations
        List<ReservationStation> reservationStations = Arrays.asList(
                addSubRS, mulDivRS, loadRS, storeRS
        );

        // Initialize Common Data Bus
        CommonDataBus cdb = new CommonDataBus();

        // Register status table
        Map<String, String> registerStatus = new HashMap<>();

        // Simulation variables
        int cycle = 1;
        int pc = 0;  // Program counter

        // Main simulation loop
        while (true) {
            System.out.println("Cycle: " + cycle);

            // Issue stage
            if (pc < instructions.size()) {
                Instruction inst = instructions.get(pc);
                boolean issued = issueInstruction(inst, reservationStations, registerFile, registerStatus, latencies);
                if (issued) {
                    pc++;
                }
            }

            // Execute stage
            for (ReservationStation rs : reservationStations) {
                rs.execute(cdb);
            }

            // Write-back stage
            cdb.broadcast(reservationStations, registerFile, registerStatus);

            // Check for completion
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
                                            RegisterFile registerFile, Map<String, String> registerStatus,
                                            Map<String, Integer> latencies) {
        String opcode = inst.getOpcode();
        ReservationStation rs = null;

        switch (opcode) {
            case "ADD.D":
            case "SUB.D":
                rs = getReservationStationByName(reservationStations, "Add/Sub");
                break;
            case "MUL.D":
            case "DIV.D":
                rs = getReservationStationByName(reservationStations, "Mul/Div");
                break;
            case "L.D":
                rs = getReservationStationByName(reservationStations, "Load");
                break;
            case "S.D":
                rs = getReservationStationByName(reservationStations, "Store");
                break;
            default:
                System.out.println("Unsupported opcode: " + opcode);
                return false;
        }

        if (rs != null && !rs.isFull()) {
            rs.issue(inst, registerFile, registerStatus, latencies.get(opcode));
            System.out.println("Issued instruction: " + inst.getOpcode() + " to " + rs.getName());
            return true;
        } else {
            return false;
        }
    }

    private static ReservationStation getReservationStationByName(List<ReservationStation> rsList, String name) {
        for (ReservationStation rs : rsList) {
            if (rs.getName().equals(name)) {
                return rs;
            }
        }
        return null;
    }
}