package com.microproject.microproject.model;

public class PipelineRegister {
    public Instruction instruction;
    public boolean stall;

    public PipelineRegister() {
        this.instruction = null;
        this.stall = false;
    }
}
