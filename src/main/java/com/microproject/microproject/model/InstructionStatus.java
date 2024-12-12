package com.microproject.microproject.model;

import javafx.beans.property.*;

public class InstructionStatus {
    private final SimpleStringProperty instruction;
    private final SimpleIntegerProperty issueCycle;
    private final SimpleIntegerProperty startExecutionCycle;
    private final SimpleIntegerProperty endExecutionCycle;
    private final SimpleIntegerProperty writeBackCycle;

    public InstructionStatus(String instruction, int issueCycle, int startExecutionCycle, int endExecutionCycle, int writeBackCycle) {
        this.instruction = new SimpleStringProperty(instruction);
        this.issueCycle = new SimpleIntegerProperty(issueCycle);
        this.startExecutionCycle = new SimpleIntegerProperty(startExecutionCycle);
        this.endExecutionCycle = new SimpleIntegerProperty(endExecutionCycle);
        this.writeBackCycle = new SimpleIntegerProperty(writeBackCycle);
    }

    public String getInstruction() {
        return instruction.get();
    }

    public void setInstruction(String instruction) {
        this.instruction.set(instruction);
    }

    public int getIssueCycle() {
        return issueCycle.get();
    }

    public void setIssueCycle(int issueCycle) {
        this.issueCycle.set(issueCycle);
    }

    public int getStartExecutionCycle() {
        return startExecutionCycle.get();
    }

    public void setStartExecutionCycle(int startExecutionCycle) {
        this.startExecutionCycle.set(startExecutionCycle);
    }

    public int getEndExecutionCycle() {
        return endExecutionCycle.get();
    }

    public void setEndExecutionCycle(int endExecutionCycle) {
        this.endExecutionCycle.set(endExecutionCycle);
    }

    public int getWriteBackCycle() {
        return writeBackCycle.get();
    }

    public void setWriteBackCycle(int writeBackCycle) {
        this.writeBackCycle.set(writeBackCycle);
    }
}