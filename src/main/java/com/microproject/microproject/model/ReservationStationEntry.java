package com.microproject.microproject.model;

public class ReservationStationEntry {
    private Instruction instruction;
    private int latency;
    private int remainingCycles;
    private double Vj;
    private double Vk;
    private String Qj;
    private String Qk;
    private String destination;
    private double result;
    private boolean executionStarted = false;
    private boolean executionComplete = false;
    private String stationName; // Add this to identify the station
    private boolean resultWritten = false;

    public ReservationStationEntry(Instruction instruction, int latency, String stationName) {
        this.instruction = instruction;
        this.latency = latency;
        this.remainingCycles = latency;
        this.destination = instruction.getDestination();
        this.stationName = stationName;
    }
    
    public boolean isResultWritten() {
        return resultWritten;
    }
    
    public void setResultWritten(boolean resultWritten) {
        this.resultWritten = resultWritten;
    }

    
    // Setter methods for operands and tags
    public void setVj(double Vj) {
        this.Vj = Vj;
    }

    public void setVk(double Vk) {
        this.Vk = Vk;
    }

    public void setQj(String Qj) {
        this.Qj = Qj;
    }

    public void setQk(String Qk) {
        this.Qk = Qk;
    }

    public boolean isReady() {
        return (Qj == null && Qk == null);
    }

    public void execute(CommonDataBus cdb) {
        if (!executionStarted && isReady()) {
            executionStarted = true;
            remainingCycles--;
        } else if (executionStarted && remainingCycles > 0) {
            remainingCycles--;
        }

        if (executionStarted && remainingCycles == 0 && !executionComplete) {
            computeResult();
            cdb.addResult(destination, result);
            executionComplete = true;
            System.out.println("Executed instruction: " + instruction.getOpcode());
        }
    }

    private void computeResult() {
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
                result = Vj; // Simulated load
                break;
            case "S.D":
            case "S.S":
                result = Vj; // Simulated store
                break;
            default:
                System.out.println("Unknown opcode: " + opcode);
                break;
        }
    }

    public boolean isExecutionComplete() {
        return executionComplete;
    }

    public String getDestination() {
        return destination;
    }

    public double getResult() {
        return result;
    }

    // Getter methods for Qj and Qk
    public String getQj() {
        return Qj;
    }

    public String getQk() {
        return Qk;
    }
}