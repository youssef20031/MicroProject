// Java
// filepath: /D:/MicroProject/src/main/java/com/microproject/microproject/simulator/TomasuloSimulator.java
package com.microproject.microproject.simulator;

import com.microproject.microproject.MainController;
import com.microproject.microproject.model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.*;

public class TomasuloSimulator extends Application {

    private int cycle = 0;
    private List<Instruction> instructions;
    private List<ReservationStation> reservationStations;
    private RegisterFile registerFile;
    private CommonDataBus cdb;
    private Map<Instruction, InstructionStatus> instructionStatuses = new HashMap<>();


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
        instructions = new ArrayList<>();
        reservationStations = new ArrayList<>();
        registerFile = new RegisterFile();
        cdb = new CommonDataBus();

        // Load instructions and latencies
        Map<String, Integer> latencies = new HashMap<>();
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

        reservationStations.addAll(Arrays.asList(addSubRS, mulDivRS, loadRS, storeRS, branchRS));

        // Initialize instructions
        instructions.add(new Instruction("DADDI", 0, "R1", "R1", "24"));
        instructions.add(new Instruction("DADDI", 0, "R2", "R2", "0"));
        instructions.add(new Instruction("L.D", 2, "F0", "0", "R1"));
        instructions.add(new Instruction("MUL.D", 4, "F4", "F0", "F2"));
        instructions.add(new Instruction("S.D", 2, "F4", "0", "R1"));
        instructions.add(new Instruction("DSUBI", 0, "R1", "R1", "8"));
        instructions.add(new Instruction("BNE", 2, "R1", "R2", "2"));
    }

    public void nextCycle() {
        cycle++;
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
        cdb.broadcast(reservationStations, registerFile, new HashMap<>());

        // Remove completed entries
        for (ReservationStation rs : reservationStations) {
            rs.removeCompletedEntries();
        }

        // Issue stage
        if (!instructions.isEmpty()) {
            Instruction inst = instructions.get(0);
            boolean issued = issueInstruction(inst);
            if (issued) {
                instructions.remove(0);
            } else {
                System.out.println("Instruction " + inst.getOpcode() + " is waiting to be issued.");
            }
        }

        // Check for completion
        boolean done = instructions.isEmpty();
        for (ReservationStation rs : reservationStations) {
            if (!rs.isEmpty()) {
                done = false;
                break;
            }
        }

        if (done) {
            System.out.println("Simulation completed in " + cycle + " cycles.");
        }
    }

    private boolean issueInstruction(Instruction inst) {
        String opcode = inst.getOpcode();
        ReservationStation rs = null;

        switch (opcode) {
            case "DADDI":
            case "DSUBI", "ADD.D", "SUB.D":
                rs = getReservationStationByName("Add/Sub");
                break;
            case "MUL.D":
            case "DIV.D":
                rs = getReservationStationByName("Mul/Div");
                break;
            case "L.D":
            case "LD":
                rs = getReservationStationByName("Load");
                break;
            case "S.D":
            case "SD":
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
            rs.issue(inst, registerFile, new HashMap<>(), inst.getLatency());
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
        return new ArrayList<>(instructionStatuses.values());
    }

    public List<ReservationStationEntry> getReservationStationEntries() {
        List<ReservationStationEntry> entries = new ArrayList<>();
        for (ReservationStation rs : reservationStations) {
            entries.addAll(rs.getEntries());
        }
        return entries;
    }

    public static void main(String[] args) {
        launch();
    }
}