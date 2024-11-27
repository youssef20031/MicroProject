package com.microproject.microproject.model;

public class Instruction {
    private String opcode;
    private int latency;
    private String destination;
    private String source1;
    private String source2;

    // Default constructor
    public Instruction() {
    }

    // Parameterized constructor
    public Instruction(String opcode, int latency, String destination, String source1, String source2) {
        this.opcode = opcode;
        this.latency = latency;
        this.destination = destination;
        this.source1 = source1;
        this.source2 = source2;
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
}