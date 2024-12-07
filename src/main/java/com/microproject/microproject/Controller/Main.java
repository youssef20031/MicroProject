package com.microproject.microproject.Controller;

import com.microproject.microproject.model.*;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Initialize Register File
        RegisterFile registerFile = new RegisterFile();

        // Construct the cache
        Cache cache = new Cache(4, 16, 2, 10);

        // Load data into cache
        Cache.loadBlockWithData(0, 10);
        //Cache.loadBlockWithData(4, 20);

        // Set initial value of R1 and R2
        Register[] integerRegisterFile = registerFile.getIntegerRegisterFile();
        integerRegisterFile[1] = new Register("R1", 4, new ArrayList<>());
        integerRegisterFile[2] = new Register("R2", 10, new ArrayList<>());

        // Prepare instructions
        List<Instruction> instructions = new ArrayList<>();
        instructions.add(new Instruction("LD", 0, "R0", "5", "0"));// L.D F0, 0(R1)
        instructions.add(new Instruction("LD", 0, "R2", "0", "R0"));// L.D F2, 0(R2)
        //instructions.add(new Instruction("MUL.D", 0, "F4", "F0", "F2"));
//        instructions.add(new Instruction("DADDI", 0, "R3", "R1", "R2"));
        //instructions.add(new Instruction("S.D", 0, "F4", "0", "R1"));     // S.D F4, 0(R1)

        // Add more instructions as needed
        // instructions.add(new Instruction("ADDI", 0, "R4", "R3", "10"));
        // instructions.add(new Instruction("SUB", 0, "R5", "R4", "R1"));
        // instructions.add(new Instruction("SD", 0, "R1", "0", "0"));
        //instructions.add(new Instruction("LW", 0, "R6", "0", "R1"));
        //instructions.add(new Instruction("SW", 0, "R6", "0", "0"));
        // Latency for each operation
        Map<String, Integer> latencies = new HashMap<>();
        latencies.put("L.D", 2);
        latencies.put("MUL.D", 4);
        latencies.put("ADD.D", 5);
        latencies.put("S.D", 2);
        latencies.put("DADDI", 2);
        latencies.put("LD", 2);

        // Initialize Reservation Stations
        ReservationStation addSubRS = new ReservationStation(3, "Add/Sub");
        ReservationStation mulDivRS = new ReservationStation(2, "Mul/Div");
        ReservationStation loadRS = new ReservationStation(2, "Load");
        ReservationStation storeRS = new ReservationStation(2, "Store");

        // Initialize Integer Reservation Stations
        ReservationStation integerAddSubRS = new ReservationStation(3, "Add/SubI");
        ReservationStation integerMulDivRS = new ReservationStation(2, "Mul/DivI");
        ReservationStation integerLoadRS = new ReservationStation(2, "LoadI");
        ReservationStation integerStoreRS = new ReservationStation(2, "StoreI");

        // List of reservation stations
        List<ReservationStation> reservationStations = Arrays.asList(
                addSubRS, mulDivRS, loadRS, storeRS, integerAddSubRS, integerMulDivRS, integerLoadRS, integerStoreRS
        );

        // Initialize Common Data Bus
        CommonDataBus cdb = new CommonDataBus();

        // Register status table
        Map<String, String> registerStatus = new HashMap<>();

        // Simulation variables
        int cycle = 1;
        int pc = 0;  // Program counter

        // Initialize IntegerPipeline
//        IntegerPipeline integerPipeline = new IntegerPipeline();

        // Main simulation loop
        while (true) {
            System.out.println("Cycle: " + cycle);

            // Write-back stage for floating-point instructions
            for (ReservationStation rs : reservationStations) {
                rs.writeBack(cdb, registerFile);
            }

            // Execute stage for floating-point instructions
            for (ReservationStation rs : reservationStations) {
                rs.execute(registerFile);
            }

            // Broadcast results
            cdb.broadcast(reservationStations, registerFile, registerStatus);

            // Remove completed entries
            for (ReservationStation rs : reservationStations) {
                rs.removeCompletedEntries();
            }

            // Inside your main simulation loop


//// Write Back Stage for Integer Pipeline
//            integerPipeline.writeBackStage(registerFile);
//
//// Memory Stage for Integer Pipeline
//            integerPipeline.memoryStage(registerFile, cache);
//
//// Execute Stage for Integer Pipeline
//            integerPipeline.executeStage(registerFile);
//
//// Decode Stage for Integer Pipeline
//            integerPipeline.decodeStage(registerFile);
//
//// Fetch Stage for Integer Pipeline
//            integerPipeline.fetchStage();


            // Issue stage
            if (pc < instructions.size()) {
                Instruction inst = instructions.get(pc);
//                if (inst.isIntegerInstruction()) {
//                    // Send integer instructions to integer pipeline
////                    integerPipeline.fetch(inst);
//                    pc++;
//                } else {
                    // Existing Tomasulo issue logic for floating-point instructions
                    boolean issued = issueInstruction(inst, reservationStations, registerFile, registerStatus, latencies);
                    if (issued) {
                        pc++;
                    } else {
                        System.out.println("Instruction " + inst.getOpcode() + " is waiting to be issued.");
                    }
//                }
            }

            registerFile.printStatus();
            System.out.println("Cache Status:");
            System.out.println(cache);

            // Check for completion
            boolean done = (pc >= instructions.size());
            for (ReservationStation rs : reservationStations) {
                if (!rs.isEmpty()) {
                    done = false;
                    break;
                }
            }
//            // Also check if integer pipeline is empty
//            if (!integerPipeline.isEmpty()) {
//                done = false;
//            }

            if (done) {
                break;
            }

            cycle++;
            if (cycle == 100)
                break;
            System.out.println("-----------------------------");
        }

        System.out.println("Simulation completed in " + cycle + " cycles.");
    }

    private static boolean issueInstruction(Instruction inst, List<ReservationStation> reservationStations,
                                            RegisterFile registerFile, Map<String, String> registerStatus,
                                            Map<String, Integer> latencies) {
        String opcode = inst.getOpcode();
        ReservationStation rs = null;

        switch (opcode) {
            case "DADDI":
            case "SUBI":
            case "ADDI":
                rs = getReservationStationByName(reservationStations, "Add/SubI");
                break;
            case "ADD.D":
            case "SUB.D":
                rs = getReservationStationByName(reservationStations, "Add/Sub");
                break;
            case "MUL.D":
            case "DIV.D":
                rs = getReservationStationByName(reservationStations, "Mul/Div");
                break;
            case "LD":
            case "LW":
                rs = getReservationStationByName(reservationStations, "LoadI");
                break;
            case "L.D":
            case "L.S":
                rs = getReservationStationByName(reservationStations, "Load");
                break;
            case "SD":
            case "SW":
                rs = getReservationStationByName(reservationStations, "StoreI");
                break;
            case "S.D":
            case "S.S":
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