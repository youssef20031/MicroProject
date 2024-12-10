package com.microproject.microproject.Controller;

import com.microproject.microproject.model.*;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Initialize Register File
        RegisterFile registerFile = new RegisterFile();

        // Construct the cache
        Cache cache = new Cache(8, 256, 2, 10);

        // Load data into cache
//        Cache.loadBlockWithData(0, 10);
//        Cache.loadBlockWithData(8, 20);
//        Cache.loadBlockWithData(16, 20);
//        Cache.loadBlockWithData(24, 20);

        // Set initial value of R1 and R2
        Register[] integerRegisterFile = registerFile.getIntegerRegisterFile();
//        integerRegisterFile[1] = new Register("R1", 4, new ArrayList<>());
//        integerRegisterFile[2] = new Register("F4", 2, new ArrayList<>());

        Register[] floatRegisterFile = registerFile.getFloatRegisterFile();
        floatRegisterFile[2] = new Register("F2", 2, new ArrayList<>());

        // Prepare instructions
        List<Instruction> instructions = new ArrayList<>();

        instructions.add(new Instruction("DADDI", 0, "R1", "R1", "25"));
        instructions.add(new Instruction("DADDI", 0, "R2", "R2", "0"));

        instructions.add(new Instruction("L.D", 0, "F0", "0", "R1"));
        instructions.add(new Instruction("MUL.D", 0, "F4", "F0", "F2"));
        instructions.add(new Instruction("S.D", 0, "F4", "0", "R1"));
        instructions.add(new Instruction("DSUBI", 0, "R1", "R1", "R1"));
//        instructions.add(new Instruction("BNE", 0, "R1", "R2", "2"));


        // Latency for each operation
        Map<String, Integer> latencies = new HashMap<>();
        latencies.put("L.D", 2);
        latencies.put("MUL.D", 4);
        latencies.put("DIV.D", 4);
        latencies.put("ADD.D", 5);
        latencies.put("SUB.D", 5);
        latencies.put("S.D", 2);
        latencies.put("DADDI", 2);
        latencies.put("LD", 2);
        latencies.put("BNE", 2);
        latencies.put("BEQ", 2);
        latencies.put("DSUBI", 2);

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

        // Branch
        ReservationStation branchRS = new ReservationStation(3, "Branch");

        // List of reservation stations
        List<ReservationStation> reservationStations = Arrays.asList(
                addSubRS, mulDivRS, loadRS, storeRS, integerAddSubRS, integerMulDivRS, integerLoadRS, integerStoreRS, branchRS
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
            boolean branchTaken = false;
            // Check for branch resolution
            for (ReservationStation rs : reservationStations) {
                if (rs.getName().equals("Branch")) {
                    for (ReservationStationEntry entry : rs.getEntries()) {
                        if (entry.isExecutionComplete() && !entry.isResultWritten()) {
                            if (entry.isBranchTaken()) {
                                pc = entry.getBranchTarget();
                                branchTaken = true;
                            }
                            entry.setResultWritten(true);
                            break;
                        }
                    }
                    System.out.print("Branch RS: ");
                    rs.printStatus();
                }
            }

            System.out.println("pc: " + pc);
            // Issue stage
            if (!branchTaken && pc < instructions.size()) {
                Instruction inst = instructions.get(pc);
                // Existing Tomasulo issue logic for floating-point instructions
                boolean issued = issueInstruction(inst, reservationStations, registerFile, registerStatus, latencies);
                if (issued) {
                    pc++;
                } else {
                    System.out.println("Instruction " + inst.getOpcode() + " is waiting to be issued.");
                }

            }
            // else{
            //     break;
            // }

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
            case "DSUBI":
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
            case "BNE":
            case "BEQ":
                rs = getReservationStationByName(reservationStations, "Branch");
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