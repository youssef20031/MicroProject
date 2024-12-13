package com.microproject.microproject.model;

import java.util.ArrayList;
import java.util.stream.Collectors;

import com.microproject.microproject.model.Cache;

import static com.microproject.microproject.model.ReadAddressFromText.readAddresses;

public class ReservationStationEntry {
    private String reservationStationName; // New property
    private boolean busy;                   // New property
    private String operation;
    private Instruction instruction;
    private int latency;
    private ArrayList<Pair> Qk;
    private double Vj;
    private double Vk;
    private ArrayList<Pair> Qj;
    private String destination;
    private double result;
    private boolean executionStarted = false;
    private boolean executionComplete = false;
    private boolean resultWritten = false;
    private boolean branchTaken;
    private int branchTarget;
    private int immediate;


    public ReservationStationEntry(String reservationStationName, Instruction instruction, int latency) {
        this.instruction = instruction;
        this.reservationStationName = reservationStationName;
        this.busy = true;
        this.latency = latency;
        this.remainingCycles = latency;
        this.destination = instruction.getDestination();
        this.Qj = new ArrayList<>();
        this.Qk = new ArrayList<>();
    }

    public int getImmediate() {
        return immediate;
    }
    
    public void setImmediate(int immediate) {
        this.immediate = immediate;
    }

    public boolean isBranchTaken() {
        return branchTaken;
    }
    
    public int getBranchTarget() {
        return branchTarget;
    }

    public int getLatency() {
        return latency;
    }

    public void setLatency(int latency) {
        this.latency = latency;
    }

    public void setInstruction(Instruction instruction) {
        this.instruction = instruction;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public boolean isExecutionStarted() {
        return executionStarted;
    }

    public void setExecutionStarted(boolean executionStarted) {
        this.executionStarted = executionStarted;
    }

    public void setExecutionComplete(boolean executionComplete) {
        this.executionComplete = executionComplete;
    }


    public int getRemainingCycles() {
        return remainingCycles;
    }

    public void setRemainingCycles(int remainingCycles) {
        this.remainingCycles = remainingCycles;
    }

    private int remainingCycles;

    public double getVj() {
        return Vj;
    }


    public double getVk() {
        return Vk;
    }


    public ArrayList<Pair> getQj() {
        return Qj;
    }


    public ArrayList<Pair> getQk() {
        return Qk;
    }

    // Setter methods for operands and tags
    public void setVj(double Vj) {
        this.Vj = Vj;
    }

    public void setVk(double Vk) {
        this.Vk = Vk;
    }

    public void setQj(ArrayList<Pair> Qj) {
        this.Qj = Qj;
    }

    public void setQk(ArrayList<Pair> Qk) {
        this.Qk = Qk;
    }

    public void addQj(Pair Qj) {
        if (this.Qj == null) {
            this.Qj = new ArrayList<>();
        }
        this.Qj.add(Qj);
    }

    public void addQk(Pair Qk) {
        if (this.Qk == null) {
            this.Qk = new ArrayList<>();
        }
        this.Qk.add(Qk);
    }

    // ReservationStationEntry.java
    public boolean isReady(RegisterFile registerFile) {
        return (Qj == null || Qj.isEmpty()) && (Qk == null || Qk.isEmpty());
    }

    public void execute(RegisterFile registerFile) {
        if (!executionStarted && isReady(registerFile)) {
            executionStarted = true;
            System.out.println("Instruction " + instruction.getOpcode() + " started execution.");
            if (instruction.getOpcode().equals("L.D") || instruction.getOpcode().equals("L.S") ||
                    instruction.getOpcode().equals("LD") || instruction.getOpcode().equals("LW")) {

                // Assuming Vk holds the effective address for load instructions
                int cacheSlotAddress = (int) Vk;
                // Check if the cache slot at the effective address is empty
                if (Cache.isSlotEmpty(cacheSlotAddress)) {
                    // Cache miss: Slot is empty
                    //Cache.gotAccessed(); // Mark cache as accessed
                    String filePath = "src/main/java/com/microproject/microproject/text/address.txt";
                    ArrayList<int[]> addresses = readAddresses(filePath);

                    //search for the address in the text file
                    for (int[] address : addresses) {
                        if (address[0] == cacheSlotAddress) {
                            Cache.loadBlockWithData(address[0], address[1]);
                            break;
                        }
                    }
                    remainingCycles = Math.max(remainingCycles, Cache.getCacheMissPenalty());
                    System.out.println("Cache miss at address " + cacheSlotAddress + ". Applying miss penalty.");
                } else {
                    // Cache hit: Slot contains valid data
                    System.out.println("Cache hit at address " + cacheSlotAddress + ".");
                }
            }
        }
        if (executionStarted && !executionComplete) {
            if (remainingCycles > 0) {
                remainingCycles--;
                System.out.println("Instruction " + instruction.getOpcode() + " executing. Remaining cycles: " + remainingCycles);
            }
            if (remainingCycles == 0) {
                computeResult(registerFile);
                executionComplete = true;
                System.out.println("Execution complete for: " + instruction.getOpcode());
            }
        }
    }

// ReservationStationEntry.java

    private void computeResult(RegisterFile registerFile) {
        String opcode = instruction.getOpcode();
        switch (opcode) {
            case "DADDI":
            case "ADDI":
            case "ADD.D":
            case "ADD.S":
                result = Vj + Vk;
                break;
            case "DSUBI":
            case "SUB.D":
            case "SUB.S":
                result = Vj - Vk;
                break;
            case "MUL.D":
            case "MUL.S":
                result = Vj * Vk;
                break;
            case "DIV.D":
            case "DIV.S":
                result = Vj / Vk;
                break;
            case "LD":
            case "LW":
            case "L.D":
            case "L.S":
                // Load data from cache using the effective address in Vk
//                int loadAddress = (int) Vk;
                int loadAddress = this.getInstruction().getEffectiveAddress();
                Cache.accessData(loadAddress); // Simulate cache latency
                result = Cache.readData(loadAddress);
                break;
            case "S.D":
            case "S.S":
            case "SW":
            case "SD":
                // Store data to cache using the effective address in Vk and data in Vj
                int storeAddress = (int) Vk;
                Cache.accessData(storeAddress); // Simulate cache latency
                Cache.writeData(storeAddress, Vj);
                break;
            case "BNE":
                branchTaken = (Vj != Vk);
                branchTarget = immediate;
                break;
            case "BEQ":
                branchTaken = (Vj == Vk);
                branchTarget = immediate;
                break;
            default:
                System.out.println("Unknown opcode: " + opcode);
                break;
        }
    }

    public boolean isExecutionComplete() {
        return executionComplete;
    }

    public boolean isResultWritten() {
        return resultWritten;
    }

    public void setResultWritten(boolean resultWritten) {
        this.resultWritten = resultWritten;
    }

    public String getDestination() {
        return destination;
    }

    public double getResult() {
        return result;
    }

    public Instruction getInstruction() {
        return instruction;
    }

    public static class Pair{
        String reservationStationName;
        int instructionNumber;

        public Pair(String reservationStationName, int instructionNumber){
            this.reservationStationName = reservationStationName;
            this.instructionNumber = instructionNumber;
        }

        public String getReservationStationName() {
            return reservationStationName;
        }
        public int getInstructionNumber() {
            return instructionNumber;
        }
        @Override
        public String toString() {
            return reservationStationName + ", " + instructionNumber;
        }
    }

    // Getter for reservationStationName
    public String getReservationStationName() {
        return reservationStationName;
    }

    // Getter for busy
    public boolean isBusy() {
        return busy;
    }

    // Setter for busy
    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    // Getter for operation
    public String getOperation() {
        return operation;
    }


    public void setOperation(String opcode) {
        this.operation = opcode;
    }


    // ReservationStationEntry.java
public String getQjString() {
    if (Qj == null || Qj.isEmpty()) {
        return "";
    }
    return Qj.stream()
             .map(Pair::toString)
             .collect(Collectors.joining(", "));
}

public String getQkString() {
    if (Qk == null || Qk.isEmpty()) {
        return "";
    }
    return Qk.stream()
             .map(Pair::toString)
             .collect(Collectors.joining(", "));
}
}