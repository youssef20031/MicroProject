package com.microproject.microproject.model;

import static com.microproject.microproject.Controller.Main.numberOfInstructions;

public class Instruction {
    private String opcode;
    private int latency;
    private String destination;
    private String source1;
    private String source2;
    private double src1Value;
    private double src2Value;
    private double result;
    private double dataToStore;
    private int effectiveAddress;
    private double LoadedData;
    private int instructionNumber;

    // Default constructor
    public Instruction() {
    }

    // Parameterized constructor
    public Instruction(String opcode, int latency, String destination, String source1, String source2, int instructionNumber) {
        this.opcode = opcode;
        this.latency = latency;
        this.destination = destination;
        this.source1 = source1;
        this.source2 = source2;
        this.instructionNumber = instructionNumber;
    }

    public int getInstructionNumber() {
        return instructionNumber;
    }

    public int getLatency() {
        return latency;
    }

    public void setLatency(int latency) {
        this.latency = latency;
    }
    // Getters and setters
    public String getOpcode() {
        return opcode;
    }

    public void setOpcode(String opcode) {
        this.opcode = opcode;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getSource1() {
        return source1;
    }

    public void setSource1(String source1) {
        this.source1 = source1;
    }

    public String getSource2() {
        return source2;
    }

    public void setSource2(String source2) {
        this.source2 = source2;
    }

    // Getters and setters for src1Value, src2Value, result
    public void setSrc1Value(double value) {
        this.src1Value = value;
    }

    public double getSrc1Value() {
        return src1Value;
    }

    public void setSrc2Value(double value) {
        this.src2Value = value;
    }

    public double getSrc2Value() {
        return src2Value;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public double getResult() {
        return result;
    }
    public double getDataToStore() {
        return dataToStore;
    }

    public void setDataToStore(double dataToStore) {
        this.dataToStore = dataToStore;
    }
    public int getEffectiveAddress() {
        return effectiveAddress;
    }

    public void setEffectiveAddress(int effectiveAddress) {
        this.effectiveAddress = effectiveAddress;
    }

}