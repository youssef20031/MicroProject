package com.microproject.microproject.model;

public class ReservationStationEntry {
    private Instruction instruction;
    private int latency;

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

    private double Vj;

    public double getVk() {
        return Vk;
    }

    private double Vk;

    public String getQj() {
        return Qj;
    }

    private String Qj;

    public String getQk() {
        return Qk;
    }

    private String Qk;
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

    public void execute() {
        if (!executionStarted && isReady()) {
            executionStarted = true;
            System.out.println("Instruction " + instruction.getOpcode() + " started execution.");
        }

        if (executionStarted && !executionComplete) {
            if (remainingCycles > 0) {
                remainingCycles--;
            }
            if (remainingCycles == 0) {
                computeResult();
                executionComplete = true;
                System.out.println("Execution complete for: " + instruction.getOpcode());
            }
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