package com.microproject.microproject.model;

import java.util.LinkedList;
import java.util.Queue;

public class IntegerPipeline {
    private Queue<Instruction> fetchQueue;
    private Queue<Instruction> decodeQueue;
    private Queue<Instruction> executeQueue;
    private Queue<Instruction> writeBackQueue;
    
    public IntegerPipeline() {
        fetchQueue = new LinkedList<>();
        decodeQueue = new LinkedList<>();
        executeQueue = new LinkedList<>();
        writeBackQueue = new LinkedList<>();
    }

    public void writeBackStage(RegisterFile registerFile) {
        if (!writeBackQueue.isEmpty()) {
            Instruction instr = writeBackQueue.poll();
            writeBack(instr, registerFile);
            System.out.println("Integer instruction " + instr.getOpcode() + " wrote back result " + instr.getResult() + " to " + instr.getDestination());
        }
    }

    public void executeStage(RegisterFile registerFile) {
        if (!executeQueue.isEmpty()) {
            Instruction instr = executeQueue.poll();
            execute(instr, registerFile);
            writeBackQueue.offer(instr);
            System.out.println("Executed integer instruction " + instr.getOpcode() + ", result: " + instr.getResult());
        }
    }

    public void decodeStage(RegisterFile registerFile) {
        if (!decodeQueue.isEmpty()) {
            Instruction instr = decodeQueue.poll();
            decode(instr, registerFile);
            executeQueue.offer(instr);
            System.out.println("Decoded integer instruction " + instr.getOpcode());
        }
    }

    public void fetchStage() {
        if (!fetchQueue.isEmpty()) {
            Instruction instr = fetchQueue.poll();
            decodeQueue.offer(instr);
            System.out.println("Fetched integer instruction " + instr.getOpcode());
        }
    }

    public void fetch(Instruction instr) {
        fetchQueue.add(instr);
    }
    
    private void decode(Instruction instr, RegisterFile registerFile) {
        // Read operands from registers
        instr.setSrc1Value(registerFile.getRegisterValue(instr.getSource1()));
        String opcode = instr.getOpcode().toUpperCase();
        if (opcode.equals("ADDI") || opcode.equals("SUBI")) {
            // Immediate value
            instr.setSrc2Value(Double.parseDouble(instr.getSource2()));
        } else {
            instr.setSrc2Value(registerFile.getRegisterValue(instr.getSource2()));
        }
    }
    
    private void execute(Instruction instr, RegisterFile registerFile) {
        // Perform ALU operation
        String opcode = instr.getOpcode().toUpperCase();
        int src1 = (int) instr.getSrc1Value();
        int src2 = (int) instr.getSrc2Value();
        int result = 0;
        switch (opcode) {
            case "ADD":
            case "ADDI":
                result = src1 + src2;
                break;
            case "SUB":
            case "SUBI":
                result = src1 - src2;
                break;
            // Add other integer operations if needed
            default:
                System.out.println("Unknown integer opcode: " + opcode);
                break;
        }
        instr.setResult(result);
    }
    
    private void writeBack(Instruction instr, RegisterFile registerFile) {
        // Write result back to register
        registerFile.setRegisterValue(instr.getDestination(), instr.getResult());
    }

    public boolean isEmpty() {
        return fetchQueue.isEmpty() && decodeQueue.isEmpty() && executeQueue.isEmpty() && writeBackQueue.isEmpty();
    }
}