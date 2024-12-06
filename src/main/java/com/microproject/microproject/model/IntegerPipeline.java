package com.microproject.microproject.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import static com.microproject.microproject.model.ReadAddressFromText.readAddresses;

public class IntegerPipeline {
    private Queue<Instruction> fetchQueue;
    private Queue<Instruction> decodeQueue;
    private Queue<Instruction> executeQueue;
    private Queue<Instruction> memoryQueue;      // Added Memory Access Stage
    private Queue<Instruction> writeBackQueue;
    
    
    public IntegerPipeline() {
        fetchQueue = new LinkedList<>();
        decodeQueue = new LinkedList<>();
        executeQueue = new LinkedList<>();
        memoryQueue = new LinkedList<>();        // Initialize Memory Access Queue
        writeBackQueue = new LinkedList<>();
    }

    public void writeBackStage(RegisterFile registerFile) {
        if (!writeBackQueue.isEmpty()) {
            Instruction instr = writeBackQueue.poll();
            writeBack(instr, registerFile);
            System.out.println("Instruction " + instr.getOpcode() + " wrote back to " + instr.getDestination());
        }
    }

    public void memoryStage(RegisterFile registerFile, Cache cache) {
        if (!memoryQueue.isEmpty()) {
            Instruction instr = memoryQueue.poll();
            memoryAccess(instr, registerFile, cache);
            // For load instructions, proceed to write-back stage
            writeBackQueue.offer(instr);

            System.out.println("Memory access for instruction " + instr.getOpcode());
        }
    }

    public void executeStage(RegisterFile registerFile) {
        if (!executeQueue.isEmpty()) {
            Instruction instr = executeQueue.poll();
            execute(instr, registerFile);
            String opcode = instr.getOpcode().toUpperCase();
            // After execute, send to memory stage if load/store, else to write-back stage

            memoryQueue.offer(instr);

            System.out.println("Executed instruction " + instr.getOpcode());
        }
    }

    public void decodeStage(RegisterFile registerFile) {
        if (!decodeQueue.isEmpty()) {
            Instruction instr = decodeQueue.poll();
            decode(instr, registerFile);
            executeQueue.offer(instr);
            System.out.println("Decoded instruction " + instr.getOpcode());
        }
    }

    public void fetchStage() {
        if (!fetchQueue.isEmpty()) {
            Instruction instr = fetchQueue.poll();
            decodeQueue.offer(instr);
            System.out.println("Fetched instruction " + instr.getOpcode());
        }
    }

    public void fetch(Instruction instr) {
        fetchQueue.offer(instr);
    }

    // Helper methods
    private void decode(Instruction instr, RegisterFile registerFile) {
        String opcode = instr.getOpcode().toUpperCase();
        if (opcode.equals("LW") || opcode.equals("LD") || opcode.equals("SW") || opcode.equals("SD")) {
            // For load/store, source1 is base register, source2 is offset or data register
            instr.setSrc1Value(registerFile.getRegisterValue(instr.getSource2())); // Base register
            instr.setSrc2Value(Double.parseDouble(instr.getSource1()));// Offset


            if (opcode.equals("SW") || opcode.equals("SD")) {
                // For store, the data to store is in the destination register
                instr.setDataToStore(registerFile.getRegisterValue(instr.getDestination()));
            }
        } else if (opcode.equals("ADDI") || opcode.equals("SUBI")) {
            instr.setSrc1Value(registerFile.getRegisterValue(instr.getSource1()));
            instr.setSrc2Value(Double.parseDouble(instr.getSource2()));
        } else {
            instr.setSrc1Value(registerFile.getRegisterValue(instr.getSource1()));
            instr.setSrc2Value(registerFile.getRegisterValue(instr.getSource2()));
        }
    }

    private void execute(Instruction instr, RegisterFile registerFile) {
        String opcode = instr.getOpcode().toUpperCase();
        double src1 = instr.getSrc1Value();
        double src2 = instr.getSrc2Value();
        double result = 0;

        switch (opcode) {
            case "ADD":
            case "ADDI":
                result = src1 + src2;
                instr.setResult(result);
                break;
            case "SUB":
            case "SUBI":
                result = src1 - src2;
                instr.setResult(result);
                break;
            case "LW":
            case "LD":
                result = src1 + src2;
                instr.setEffectiveAddress((int) result);
                if (Cache.isSlotEmpty((int) result)) {
                    // Cache miss: Slot is empty
                    //Cache.gotAccessed(); // Mark cache as accessed
                    String filePath = "src/main/java/com/microproject/microproject/text/address.txt";
                    ArrayList<int[]> addresses = readAddresses(filePath);

                    //search for the address in the text file
                    for (int[] address : addresses) {
                        if (address[0] == (int) result) {
                            Cache.loadBlockWithData(address[0], address[1]);
                            break;
                        }
                    }
                    //remainingCycles = Math.max(remainingCycles, Cache.getCacheMissPenalty());
                    System.out.println("Cache miss at address " + (int) result + ". Applying miss penalty.");
                } else {
                    // Cache hit: Slot contains valid data
                    System.out.println("Cache hit at address " + (int) result + ".");
                }
                break;
            case "SW":
            case "SD":
                // Calculate effective address
                result = src1 + src2;
                instr.setEffectiveAddress((int) result);
                break;
            // Handle other instructions
            default:
                System.out.println("Unknown opcode: " + opcode);
                break;
        }
    }

    private void memoryAccess(Instruction instr, RegisterFile registerFile, Cache cache) {
        String opcode = instr.getOpcode().toUpperCase();
        int address = instr.getEffectiveAddress();

        switch (opcode) {
            case "LW":
            case "LD":
                // Read data from cache/memory
                double loadedData = cache.readData(address);
                instr.setLoadedData(loadedData);
                System.out.println("Loaded data from address " + address + ": " + loadedData);
                break;
            case "SW":
            case "SD":
                // Write data to cache/memory
                double dataToStore = instr.getDataToStore();
                cache.writeData(address, dataToStore);
                System.out.println("Stored data to address " + address + ": " + dataToStore);
                break;
            // Handle other memory operations if any
            default:
                System.out.println("Other Operation: " + opcode);
                break;
        }
    }

    private void writeBack(Instruction instr, RegisterFile registerFile) {
        String opcode = instr.getOpcode().toUpperCase();
        if (opcode.equals("LW") || opcode.equals("LD")) {
            // Write loaded data to the destination register
            registerFile.setRegisterValue(instr.getDestination(), instr.getLoadedData());
            System.out.println("Write-back: Loaded data " + instr.getLoadedData() + " into " + instr.getDestination());
        } else {
            // Write result back to register
            registerFile.setRegisterValue(instr.getDestination(), instr.getResult());
            System.out.println("Write-back: Result " + instr.getResult() + " into " + instr.getDestination());
        }
    }

    public boolean isEmpty() {
        return fetchQueue.isEmpty() && decodeQueue.isEmpty() && executeQueue.isEmpty()
                && memoryQueue.isEmpty() && writeBackQueue.isEmpty();
    }
}