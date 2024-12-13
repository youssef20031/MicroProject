package com.microproject.microproject.simulator;

import com.microproject.microproject.MainController;
import com.microproject.microproject.model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.*;

public class TomasuloSimulator extends Application {

    private int cycle = 1;
    private int pc = 0; // Program counter
    public static int numberOfInstructions = 0; // Added numberOfInstructions

    private List<Instruction> instructions;
    private List<String[]> instructionData;
    public List<ReservationStation> reservationStations;
    public static RegisterFile registerFile;
    private CommonDataBus cdb;
    private Cache cache;
    private Map<String, Integer> latencies;
    private Map<String, String> registerStatus;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(TomasuloSimulator.class.getResource("/com/microproject/microproject/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Tomasulo Algorithm Simulator");
        stage.setScene(scene);

        // Initialize simulation components
        initializeSimulation();

        // Pass this simulator instance to the controller
        MainController controller = fxmlLoader.getController();
        controller.setSimulator(this);

        stage.show();
    }

    private void initializeSimulation() {
        cycle = 1;
        pc = 0;
        numberOfInstructions = 0; // Initialize numberOfInstructions

        instructions = new ArrayList<>();
        instructionData = new ArrayList<>();
        reservationStations = new ArrayList<>();
        registerFile = new RegisterFile();
        cdb = new CommonDataBus();
        cache = new Cache(8, 256, 2, 10);
        registerStatus = new HashMap<>();

        // Latency for each operation
        latencies = new HashMap<>();
        latencies.put("L.D", 2);
        latencies.put("MUL.D", 4);
        latencies.put("DIV.D", 4);
        latencies.put("ADD.D", 5);
        latencies.put("SUB.D", 5);
        latencies.put("S.D", 2);
        latencies.put("LD", 2);
        latencies.put("BNE", 2);
        latencies.put("BEQ", 2);
        latencies.put("DADDI", 0);
        latencies.put("DSUBI", 0);

        // Initialize Reservation Stations
        ReservationStation addSubRS = new ReservationStation(3, "Add/Sub");
        ReservationStation mulDivRS = new ReservationStation(2, "Mul/Div");
        ReservationStation loadRS = new ReservationStation(2, "Load");
        ReservationStation storeRS = new ReservationStation(2, "Store");
        ReservationStation branchRS = new ReservationStation(3, "Branch");
        ReservationStation integerAddSubRS = new ReservationStation(3, "Add/SubI");
        ReservationStation integerMulDivRS = new ReservationStation(2, "Mul/DivI");
        ReservationStation integerLoadRS = new ReservationStation(2, "LoadI");
        ReservationStation integerStoreRS = new ReservationStation(2, "StoreI");

        reservationStations.addAll(Arrays.asList(
                branchRS, integerAddSubRS, addSubRS, mulDivRS, loadRS, storeRS,
                integerMulDivRS, integerLoadRS, integerStoreRS
        ));

        // Initialize instructions
        instructionData.add(new String[]{"DADDI", "R1", "R1", "24"});
        instructionData.add(new String[]{"DADDI", "R2", "R2", "-8"});

        instructionData.add(new String[]{"L.D", "F0", "0", "R1"});
        instructionData.add(new String[]{"MUL.D", "F4", "F0", "F2"});
        instructionData.add(new String[]{"S.D", "F4", "0", "R1"});
        instructionData.add(new String[]{"DSUBI", "R1", "R1", "8"});
        instructionData.add(new String[]{"BNE", "R1", "R2", "2"});

        // Initialize Registers
        Register[] integerRegisterFile = registerFile.getIntegerRegisterFile();
        // integerRegisterFile[1] = new Register("R1", 25, new ArrayList<>());
        // integerRegisterFile[2] = new Register("R2", 1, new ArrayList<>());

        Register[] floatRegisterFile = registerFile.getFloatRegisterFile();
        floatRegisterFile[2] = new Register("F2", 2.0, new ArrayList<>());
    }

    public void nextCycle() {
        System.out.println("Cycle: " + cycle);

        // Write-back stage
        for (ReservationStation rs : reservationStations) {
            rs.writeBack(cdb, registerFile);
        }

        // Execute stage
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

        System.out.println("PC: " + pc);

        // Issue stage
        if (!branchTaken && pc < instructionData.size()) {
            String[] data = instructionData.get(pc);
            numberOfInstructions++; // Increment numberOfInstructions
            Instruction inst = new Instruction(data[0], 0, data[1], data[2], data[3]);
            instructions.add(inst);
            boolean issued = issueInstruction(inst);
            if (issued) {
                pc++;
            } else {
                System.out.println("Instruction " + inst.getOpcode() + " is waiting to be issued.");
            }
        }

        registerFile.printStatus();
        System.out.println("Cache Status:");
        System.out.println(cache);

        // Check for completion
        boolean done = (pc >= instructionData.size());
        for (ReservationStation rs : reservationStations) {
            if (!rs.isEmpty()) {
                done = false;
                break;
            }
        }

        if (done) {
            System.out.println("Simulation completed in " + cycle + " cycles.");
        } else {
            cycle++;
        }
        System.out.println("-----------------------------");
    }

    private boolean issueInstruction(Instruction inst) {
        String opcode = inst.getOpcode();
        ReservationStation rs = null;

        switch (opcode) {
            case "DADDI":
            case "SUBI":
            case "DSUBI":
            case "ADDI":
                rs = getReservationStationByName("Add/SubI");
                break;
            case "ADD.D":
            case "SUB.D":
                rs = getReservationStationByName("Add/Sub");
                break;
            case "MUL.D":
            case "DIV.D":
                rs = getReservationStationByName("Mul/Div");
                break;
            case "LD":
            case "LW":
                rs = getReservationStationByName("LoadI");
                break;
            case "L.D":
            case "L.S":
                rs = getReservationStationByName("Load");
                break;
            case "SD":
            case "SW":
                rs = getReservationStationByName("StoreI");
                break;
            case "S.D":
            case "S.S":
                rs = getReservationStationByName("Store");
                break;
            case "BNE":
            case "BEQ":
                rs = getReservationStationByName("Branch");
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

    private ReservationStation getReservationStationByName(String name) {
        for (ReservationStation rs : reservationStations) {
            if (rs.getName().equals(name)) {
                return rs;
            }
        }
        return null;
    }

    // Additional methods as needed...

    public int getCycle() {
        return cycle;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public List<ReservationStation> getReservationStations() {
        return reservationStations;
    }

    public RegisterFile getRegisterFile() {
        return registerFile;
    }

    public List<InstructionStatus> getInstructionStatuses() {
        List<InstructionStatus> statuses = new ArrayList<>();
        for (Instruction inst : instructions) {
            InstructionStatus status = new InstructionStatus(
                    inst.getOpcode(),
                    inst.getSource1(),
                    inst.getSource2(),
                    inst.getDestination()
            );
            statuses.add(status);
        }
        System.out.println("Instruction Statuses: " + statuses);
        return statuses;
    }

    public List<ReservationStationEntry> getReservationStationEntries() {
        List<ReservationStationEntry> entries = new ArrayList<>();
        for (ReservationStation rs : reservationStations) {
            entries.addAll(rs.getEntries());
        }
        return entries;
    }

    public List<RegisterStatus> getRegisterStatuses() {
        List<RegisterStatus> registerStatuses = new ArrayList<>();
        // Populate register statuses from registerFile
        for (Register reg : registerFile.getFloatRegisterFile()) {
            registerStatuses.add(new RegisterStatus(reg.getName(), String.valueOf(reg.getValue())));
        }
        for (Register reg : registerFile.getIntegerRegisterFile()) {
            registerStatuses.add(new RegisterStatus(reg.getName(), String.valueOf(reg.getValue())));
        }
        return registerStatuses;
    }

    public List<CacheEntry> getCacheEntries() {
        return cache.getEntries();
    }

    public static void main(String[] args) {
        launch();
    }
}