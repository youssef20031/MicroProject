package com.microproject.microproject.simulator;

import com.microproject.microproject.MainController;
import com.microproject.microproject.model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.microproject.microproject.util.InstructionReader;
import com.microproject.microproject.util.InstructionLatencyReader;
import com.microproject.microproject.util.ReservationStationReader;
import com.microproject.microproject.util.CacheReader;
import javafx.util.Pair;

import java.util.*;

public class TomasuloSimulator extends Application {

    public int cycle = 1;
    public int pc = 0; // Program counter
    public static int numberOfIssuedInstructions = 0;

    public List<Instruction> issuedInstructionsUI;
    public List<String[]> instructions;
    public static List<ReservationStation> reservationStations;
    public static RegisterFile registerFile;
    public CommonDataBus cdb;
    public Cache cache;
    public Map<String, Integer> latencies;
    public Map<String, String> registerStatus;
    public static boolean isBranchInProgress = false;


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(TomasuloSimulator.class.getResource("/com/microproject/microproject/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Tomasulo Algorithm Simulator");
        stage.setMaximized(true);
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
        numberOfIssuedInstructions = 0; // Initialize numberOfInstructions
        issuedInstructionsUI = new ArrayList<>();
        instructions = new ArrayList<>();
        reservationStations = new ArrayList<>();
        registerFile = new RegisterFile();
        cdb = new CommonDataBus();
        cache = null;
        registerStatus = new HashMap<>();
        // Initialize Registers
        Register[] integerRegisterFile = registerFile.getIntegerRegisterFile();
        Register[] floatRegisterFile = registerFile.getFloatRegisterFile();
        String instructionsFilePath = "src/main/java/com/microproject/microproject/text/instruction.txt";
        String latenciesFilePath = "src/main/java/com/microproject/microproject/text/latency.txt";
        //read register from file
        // Initialize Reservation Stations from file
        String reservationStationsFilePath = "src/main/java/com/microproject/microproject/text/reservationstation.txt";
        // Initialize Cache from file
        String cacheFilePath = "src/main/java/com/microproject/microproject/text/cache.txt";


        List<Integer> cacheEntries = new ArrayList<>();
        try {
            cacheEntries = CacheReader.readCacheConfig(cacheFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cache = new Cache(cacheEntries.get(0), cacheEntries.get(1), cacheEntries.get(2), cacheEntries.get(3));


        List<String[]> instructionDataCopy = new ArrayList<>();
        List<Pair<String, Integer>> latenciesCopy = new ArrayList<>();
        try {
            instructionDataCopy = InstructionReader.readInstructions(instructionsFilePath);
            latenciesCopy = InstructionLatencyReader.readLatencies(latenciesFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //loop instructionDataCopy to populate instructionData
        for (int i = 0; i < instructionDataCopy.size(); i++) {
            String[] instruction = instructionDataCopy.get(i);
            String opcode = instruction[0];
            String destination = instruction.length > 1 ? instruction[1] : "";
            String source1 = instruction.length > 2 ? instruction[2] : "";
            String source2 = instruction.length > 3 ? instruction[3] : "";
            String[] instructionData = new String[]{opcode, destination, source1, source2};
            this.instructions.add(instructionData);
        }

        //loop latenciesCopy to populate latencies
        latencies = new HashMap<>();
        for (int i = 0; i < latenciesCopy.size(); i++) {
            Pair<String, Integer> item = latenciesCopy.get(i);
            latencies.put(item.getKey(), item.getValue());
        }

        List<Pair<Integer, String>> reservationStationsCopy = new ArrayList<>();
        try {
            reservationStationsCopy = ReservationStationReader.readReservationStations(reservationStationsFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < reservationStationsCopy.size(); i++) {
            Pair<Integer, String> item = reservationStationsCopy.get(i);
            ReservationStation rs = new ReservationStation(item.getKey(), item.getValue());
            reservationStations.add(rs);
        }
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
        // Check for branch
        for (ReservationStation rs : reservationStations) {
            if (rs.getName().equals("Branch")) {
                for (ReservationStationEntry entry : rs.getEntries()) {
                    if (entry.isExecutionComplete() && !entry.isResultWritten()) {
                        if (entry.isBranchTaken()) {
                            pc = entry.getBranchTarget();
                            branchTaken = true;
                        }
                        isBranchInProgress = false;
                        entry.setResultWritten(true);
                        break;
                    }
                }
                System.out.print("Branch RS: ");
                rs.printStatus();
            }
        }

        // Issue stage
        if (!isBranchInProgress && !branchTaken && pc < instructions.size()) {
            String[] data = instructions.get(pc);
            numberOfIssuedInstructions++; // Increment numberOfInstructions
            System.out.println("Number of Instruction " + numberOfIssuedInstructions + ".");
            Instruction inst = new Instruction(data[0], 0, data[1], data[2], data[3], numberOfIssuedInstructions);
            issuedInstructionsUI.add(inst);
            System.out.println("Instruction " + inst.getInstructionNumber() + ".");
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

        boolean done = (pc >= instructions.size());
        // Check if all reservation stations are empty, if not, done will be false
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
            case "ADD.S":
            case "SUB.D":
            case "SUB.S":
                rs = getReservationStationByName("Add/Sub");
                break;
            case "MUL.D":
            case "MUL.S":
            case "DIV.D":
            case "DIV.S":
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
        }

        return false;
    }
    private ReservationStation getReservationStationByName(String name) {
        for (ReservationStation rs : reservationStations) {
            if (rs.getName().equals(name)) {
                return rs;
            }
        }
        return null;
    }

    // All down are Getters for JavaFX GUI
    public int getCycle() {
        return cycle;
    }
    public List<InstructionStatus> getInstructionStatuses() {
        List<InstructionStatus> statuses = new ArrayList<>();
        for (Instruction inst : issuedInstructionsUI) {
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