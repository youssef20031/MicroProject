package com.microproject.microproject.model;

import java.util.ArrayList;

import com.microproject.microproject.model.Cache;

public class ReservationStationEntry {
    private Instruction instruction;
    private int latency;
    private ArrayList<String> Qk;
    private double Vj;
    private double Vk;
    private ArrayList<String> Qj;
    private String destination;
    private double result;
    private boolean executionStarted = false;
    private boolean executionComplete = false;
    private boolean resultWritten = false;

    public ReservationStationEntry(Instruction instruction, int latency) {
        this.instruction = instruction;
        this.latency = latency;
        this.remainingCycles = latency;
        this.destination = instruction.getDestination();
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


    public ArrayList<String> getQj() {
        return Qj;
    }


    public ArrayList<String> getQk() {
        return Qk;
    }

    // Setter methods for operands and tags
    public void setVj(double Vj) {
        this.Vj = Vj;
    }

    public void setVk(double Vk) {
        this.Vk = Vk;
    }

    public void setQj(ArrayList<String> Qj) {
        this.Qj = Qj;
    }

    public void setQk(ArrayList<String> Qk) {
        this.Qk = Qk;
    }
    public void addQj(String Qj) {
        if (this.Qj == null) {
            this.Qj = new ArrayList<>();
        }
        this.Qj.add(Qj);
    }
    public void addQk(String Qk) {
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
            if (instruction.getOpcode().equals("L.D") || instruction.getOpcode().equals("L.S")) {
                if (!Cache.isGotAccessed()) {
                    Cache.gotAccessed();
                    remainingCycles = Math.max(remainingCycles, Cache.getCacheMissPenalty());
                }
            }
        }
        if (executionStarted && !executionComplete) {
            if (remainingCycles > 0) {
                remainingCycles--;
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
            case "ADD.D":
            case "ADD.S":
                result = Vj + Vk;
                break;
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
            case "L.D":
            case "L.S":
                // Load data from cache using the effective address in Vk
                int loadAddress = (int) Vk;
                Cache.accessData(loadAddress); // Simulate cache latency
                result = Cache.readData(loadAddress);
                break;
            case "S.D":
            case "S.S":
                // Store data to cache using the effective address in Vk and data in Vj
                int storeAddress = (int) Vk;
                Cache.accessData(storeAddress); // Simulate cache latency
                Cache.writeData(storeAddress, Vj);
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
}