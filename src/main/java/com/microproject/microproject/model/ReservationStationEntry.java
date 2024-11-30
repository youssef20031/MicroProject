package com.microproject.microproject.model;

import java.util.ArrayList;

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
    /*if (instruction.getOpcode().equals("S.D") || instruction.getOpcode().equals("S.S")) {
        // Recheck the destination register Qi
        Register destReg = registerFile.getRegister(destination);
        if (destReg != null) {
            ArrayList<String> qiList = destReg.getQi();
            if (qiList.size() >= 2) {
                return false;
            } else if (qiList.size() == 1) {
                String qi = qiList.getFirst();
                return qi.equals("Store");
            }
        }
    }*/
    return (Qj == null || Qj.isEmpty()) && (Qk == null || Qk.isEmpty());
}

    public void execute(RegisterFile registerFile) {
        if (!executionStarted && isReady(registerFile)) {
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

    public void setupOperands(RegisterFile registerFile) {
        // Handle Qj and Vj
        if (Qj != null && !Qj.isEmpty()) {
            Register reg1 = registerFile.getRegister(Qj.get(0));
            if (reg1 != null) {
                String qi = String.valueOf(reg1.getQi());
                if (qi.isEmpty()) {
                    this.Vj = reg1.getValue();
                    this.Qj = null;
                } else {
                    this.Qj.set(0, qi);
                }
            } else {
                System.err.println("Register " + Qj.get(0) + " not found.");
            }
        }

        // Handle Qk and Vk
        if (Qk != null && !Qk.isEmpty()) {
            Register reg2 = registerFile.getRegister(Qk.get(0));
            if (reg2 != null) {
                String qi = String.valueOf(reg2.getQi());
                if (qi.isEmpty()) {
                    this.Vk = reg2.getValue();
                    this.Qk = null;
                } else {
                    this.Qk.set(0, qi);
                }
            } else {
                System.err.println("Register " + Qk.get(0) + " not found.");
            }
        }
    }
}