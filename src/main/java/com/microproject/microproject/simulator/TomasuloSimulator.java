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
import com.microproject.microproject.util.RegisterReader;
import javafx.util.Pair;

import java.util.*;

public class TomasuloSimulator extends Application {

    public int cycle = 1;
    public int pc = 0; // Program counter
    public static int numberOfInstructions = 0; // Added numberOfInstructions

    public List<Instruction> instructions;
    public List<String[]> instructionData;
    public static List<ReservationStation> reservationStations;
    public static RegisterFile registerFile;
    public CommonDataBus cdb;
    public Cache cache;
    public Map<String, Integer> latencies;
    public Map<String, String> registerStatus;

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
    public static boolean branchInProgress = false;
    private void initializeSimulation() {
        cycle = 1;
        pc = 0;
        numberOfInstructions = 0; // Initialize numberOfInstructions

        instructions = new ArrayList<>();
        instructionData = new ArrayList<>();
        reservationStations = new ArrayList<>();
        registerFile = new RegisterFile();
        cdb = new CommonDataBus();
        cache = null;
        registerStatus = new HashMap<>();

        // Initialize Cache from file
        String cacheFilePath = "src/main/java/com/microproject/microproject/text/cache.txt";
        List<Integer> cacheEntries = new ArrayList<>();
        try {
            cacheEntries = CacheReader.readCacheConfig(cacheFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cache = new Cache(cacheEntries.get(0), cacheEntries.get(1), cacheEntries.get(2), cacheEntries.get(3));

        String instructionsFilePath = "src/main/java/com/microproject/microproject/text/instruction.txt";
        String latenciesFilePath = "src/main/java/com/microproject/microproject/text/latency.txt";
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
            this.instructionData.add(instructionData);
        }

        //loop latenciesCopy to populate latencies
        latencies = new HashMap<>();
        for (int i = 0; i < latenciesCopy.size(); i++) {
            Pair<String, Integer> item = latenciesCopy.get(i);
            latencies.put(item.getKey(), item.getValue());
        }



        // Latency for each operation
        /*latencies = new HashMap<>();
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
        latencies.put("DSUBI", 0);*/

        // Initialize Reservation Stations from file
        String reservationStationsFilePath = "src/main/java/com/microproject/microproject/text/reservationstation.txt";
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




        //List<String[]> instr = new ArrayList<>();
        /*instructionData.add(new String[]{"L.D", "F6", "0", "0"});
        instructionData.add(new String[]{"ADD.D", "F7", "F1", "F3"});
        instructionData.add(new String[]{"L.D", "F2", "20", "0"});
        instructionData.add(new String[]{"MUL.D", "F0", "F2", "F4"});
        instructionData.add(new String[]{"SUB.D", "F8", "F2", "F6"});
        instructionData.add(new String[]{"DIV.D", "F10", "F0", "F6"});
        instructionData.add(new String[]{"S.D", "F10", "0", "0"});*/
        // Initialize Reservation Stations
        /*ReservationStation addSubRS = new ReservationStation(3, "Add/Sub");
        ReservationStation mulDivRS = new ReservationStation(2, "Mul/Div");
        ReservationStation loadRS = new ReservationStation(2, "Load");
        ReservationStation storeRS = new ReservationStation(2, "Store");
        ReservationStation branchRS = new ReservationStation(3, "Branch");
        ReservationStation integerAddSubRS = new ReservationStation(3, "Add/SubI");
        ReservationStation integerMulDivRS = new ReservationStation(2, "Mul/DivI");
        ReservationStation integerLoadRS = new ReservationStation(2, "LoadI");
        ReservationStation integerStoreRS = new ReservationStation(2, "StoreI");*/

        /*reservationStations.addAll(Arrays.asList(
                branchRS, integerAddSubRS, addSubRS, mulDivRS, loadRS, storeRS,
                integerMulDivRS, integerLoadRS, integerStoreRS
        ));*/

        // Initialize Registers
        Register[] integerRegisterFile = registerFile.getIntegerRegisterFile();
        // integerRegisterFile[1] = new Register("R1", 25, new ArrayList<>());
        // integerRegisterFile[2] = new Register("R2", 1, new ArrayList<>());

        Register[] floatRegisterFile = registerFile.getFloatRegisterFile();
        //floatRegisterFile[1] = new Register("F0", 2.0, new ArrayList<>());
        //floatRegisterFile[2] = new Register("F2", 4.0, new ArrayList<>());
        //floatRegisterFile[4] = new Register("F4", 1.5, new ArrayList<>());

        //read register from file
        String registerFilePath = "src/main/java/com/microproject/microproject/text/register.txt";
        List<Pair<String, Integer>> registerFileCopy = new ArrayList<>();
        try {
            registerFileCopy = RegisterReader.readRegisters(registerFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //check whether the register is integer or float
        for (int i = 0; i < registerFileCopy.size(); i++) {
            Pair<String, Integer> item = registerFileCopy.get(i);
            if (item.getKey().charAt(0) == 'F') {
                floatRegisterFile[Integer.parseInt(item.getKey().substring(1))] = new Register(item.getKey(), item.getValue(), new ArrayList<>());
            } else {
                integerRegisterFile[Integer.parseInt(item.getKey().substring(1))] = new Register(item.getKey(), item.getValue(), new ArrayList<>());
            }
        }




        // Initialize instructions
        /*instructionData.add(new String[]{"DADDI", "R1", "R1", "24"});
        instructionData.add(new String[]{"DADDI", "R2", "R2", "-8"});

        instructionData.add(new String[]{"L.D", "F0", "0", "R1"});
        instructionData.add(new String[]{"MUL.D", "F4", "F0", "F2"});
        instructionData.add(new String[]{"S.D", "F4", "0", "R1"});
        instructionData.add(new String[]{"DSUBI", "R1", "R1", "8"});
        instructionData.add(new String[]{"BNE", "R1", "R2", "2"});*/

       }

    public void nextCycle() {
        if(cycle == 22)
            System.out.println();
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
                        branchInProgress = false;
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
        if (!branchInProgress && !branchTaken && pc < instructionData.size()) {
            String[] data = instructionData.get(pc);
            numberOfInstructions++; // Increment numberOfInstructions
            System.out.println("Number of Instruction " + numberOfInstructions + ".");
            Instruction inst = new Instruction(data[0], 0, data[1], data[2], data[3], numberOfInstructions);
            instructions.add(inst);
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
            //return;
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