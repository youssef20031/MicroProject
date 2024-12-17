package com.microproject.microproject.model;

public class CDBEntry {
    private Instruction instruction;
    private String destination;
    private double result;
    private String src2;
    private int instructionNumber;

    // Overloaded constructor for instructions like S.D that need to broadcast src2
    public CDBEntry(Instruction instruction, String destination, double result, String src2, int instructionNumber) {
        this.instruction = instruction;
        this.destination = destination;
        this.result = result;
        this.src2 = src2;
        this.instructionNumber = instructionNumber;
    }

    // Constructor for instructions that have a destination register
    public CDBEntry(Instruction instruction, String destination, double result, int instructionNumber) {
        this.instruction = instruction;
        this.destination = destination;
        this.result = result;
        this.instructionNumber = instructionNumber;
    }

    public int getInstructionNumber() {
        return instructionNumber;
    }

    public String getDestination() {
        return destination;
    }

    public double getResult() {
        return result;
    }

    public String getSrc2() {
        return src2;
    }
}
