package com.microproject.microproject.model;

import javafx.beans.property.*;

public class InstructionStatus {
    private final SimpleStringProperty instruction;
    private final SimpleStringProperty opcode;
    private final SimpleStringProperty destination;
    private final SimpleStringProperty source1;
    private final SimpleStringProperty source2;

    public InstructionStatus(String opcode, String source1, String source2, String destination) {
        this.instruction = new SimpleStringProperty(opcode);
        this.opcode = new SimpleStringProperty(opcode);
        this.destination = new SimpleStringProperty(destination);
        this.source1 = new SimpleStringProperty(source1);
        this.source2 = new SimpleStringProperty(source2);
    }

    public String getInstruction() {
        return instruction.get();
    }

    public void setInstruction(String instruction) {
        this.instruction.set(instruction);
    }

    public String getOpcode() {
        return opcode.get();
    }

    public void setOpcode(String opcode) {
        this.opcode.set(opcode);
    }

    public String getDestination() {
        return destination.get();
    }

    public void setDestination(String destination) {
        this.destination.set(destination);
    }

    public String getSource1() {
        return source1.get();
    }

    public void setSource1(String source1) {
        this.source1.set(source1);
    }

    public String getSource2() {
        return source2.get();
    }
    public void setSource2(String source2) {
        this.source2.set(source2);
    }
}