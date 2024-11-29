// Java
package com.microproject.microproject.model;

import java.util.ArrayList;
import java.util.List;

public class InstructionQueue {
    private List<Row> table;


    public InstructionQueue() {
        this.table = new ArrayList<>();
    }

    public void addInstruction(Instruction instruction, int issueCycle, int startExecutionCycle, int endExecutionCycle, int writeCycle) {
        this.table.add(new Row(instruction, issueCycle, startExecutionCycle, endExecutionCycle, writeCycle));
    }

    public List<Row> getTable() {
        return table;
    }

    public static class Row {
        private Instruction instruction;
        private int issueCycle;
        private int startExecutionCycle;
        private int endExecutionCycle;
        private int writeCycle;

        public Row(Instruction instruction, int issueCycle, int startExecutionCycle, int endExecutionCycle, int writeCycle) {
            this.instruction = instruction;
            this.issueCycle = issueCycle;
            this.startExecutionCycle = startExecutionCycle;
            this.endExecutionCycle = endExecutionCycle;
            this.writeCycle = writeCycle;
        }

        public Instruction getInstruction() {
            return instruction;
        }

        public int getIssueCycle() {
            return issueCycle;
        }

        public int getStartExecutionCycle() {
            return startExecutionCycle;
        }

        public int getEndExecutionCycle() {
            return endExecutionCycle;
        }

        public int getWriteCycle() {
            return writeCycle;
        }
    }
}