package com.microproject.microproject.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ReservationStation {
    private final int capacity;
    private final String name;


    private final List<ReservationStationEntry> entries;

    public ReservationStation(int capacity, String name) {
        this.capacity = capacity;
        this.name = name;
        this.entries = new ArrayList<>();
    }

    public List<ReservationStationEntry> getEntries() {
        return entries;
    }
    public String getName() {
        return name;
    }

    public boolean isFull() {
        return entries.size() >= capacity;
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    public void issue(Instruction inst, RegisterFile registerFile, Map<String, String> registerStatus, int latency) {
        // Create a new entry
        ReservationStationEntry entry = new ReservationStationEntry(inst, latency);

        // Set the actual destination register name
        String opcode = inst.getOpcode();
        String destination = inst.getDestination();
        entry.setDestination(destination);

        // Get source registers
        String src1 = inst.getSource1();
        String src2 = inst.getSource2();
        String dest = destination;


        if (opcode.equals("L.D") || opcode.equals("L.S") || opcode.equals("LD") || opcode.equals("LW")) {

            if (src2 != null && !src2.isEmpty()) {
                ArrayList<String> src2Qi = registerFile.getRegisterQi(src2);
                if (src2Qi != null && !src2Qi.isEmpty()) {
                    entry.addQk(src2Qi.getLast());
                } else {
                    double baseValue = registerFile.getRegisterValue(src2);
                    int offset = Integer.parseInt(src1);
                    double effectiveAddress = baseValue + offset;
                    entry.setVk(effectiveAddress);
                }
            }

            // Destination register Qi is set to this reservation station
            //registerFile.setRegisterQi(dest, this.name);
        } else if (opcode.equals("S.D") || opcode.equals("S.S") || opcode.equals("SD") || opcode.equals("SW")) {
            // For Store: Compute effective address Vk = base register value + offset
            double baseValue = registerFile.getRegisterValue(src2);
            int offset = Integer.parseInt(src1);
            double effectiveAddress = baseValue + offset;
            entry.setVk(effectiveAddress);

            // Get data to store from the destination register
            if (registerFile.getRegisterQi(dest) != null && !registerFile.getRegisterQi(dest).isEmpty()) {
                ArrayList<String> destQi = registerFile.getRegisterQi(dest);
                if (destQi != null && !destQi.isEmpty()) {
                    entry.addQj(destQi.getLast());
                }
            } else {
                entry.setVj(registerFile.getRegisterValue(dest));
            }
        }// Handle Branch instructions
    else if (opcode.equals("BNE") || opcode.equals("BEQ")) {
        // destination is the first register, src1 is the second register, src2 is the branch target (offset)
        // Handle first register dependencies
        if (destination != null && !destination.isEmpty()) {
            ArrayList<String> destQi = registerFile.getRegisterQi(destination);
            if (destQi != null && !destQi.isEmpty()) {
                entry.addQj(destQi.get(destQi.size() - 1));
            } else {
                entry.setVj(registerFile.getRegisterValue(destination));
            }
        }

        // Handle second register dependencies
        if (src1 != null && !src1.isEmpty()) {
            ArrayList<String> src1Qi = registerFile.getRegisterQi(src1);
            if (src1Qi != null && !src1Qi.isEmpty()) {
                entry.addQk(src1Qi.get(src1Qi.size() - 1));
            } else {
                entry.setVk(registerFile.getRegisterValue(src1));
            }
        }

        // The branch target (src2) is an immediate value (offset), so store it
        entry.setImmediate(Integer.parseInt(src2));
    }
        else{
            // Check Qi for source1 from registerFile
            if (src1 != null && !src1.isEmpty()) {
                ArrayList<String> src1Qi = registerFile.getRegisterQi(src1);
                if (src1Qi != null && !src1Qi.isEmpty()) {
                    entry.addQj(src1Qi.getLast());
                } else {
                    entry.setVj(registerFile.getRegisterValue(src1));
                }
            }

            // Check Qi for source2 from registerFile
            if (src2 != null && !src2.isEmpty()) {
                ArrayList<String> src2Qi = registerFile.getRegisterQi(src2);
                if (src2Qi != null && !src2Qi.isEmpty()) {
                    entry.addQk(src2Qi.getLast());
                } else {
                    if(src2.charAt(0) != 'F' && src2.charAt(0) != 'R')
                        entry.setVk(Integer.parseInt(src2));
                    else
                        entry.setVk(registerFile.getRegisterValue(src2));
                }
            }
        }
        registerFile.setRegisterQi(destination, this.name);
        entries.add(entry);
        //System.out.println("Issued instruction: " + opcode + " to " + this.name);

    }


    // src/main/java/com/microproject/microproject/model/ReservationStation.java

    public void execute(RegisterFile registerFile) {
        for (ReservationStationEntry entry : entries) {
            entry.execute(registerFile);
        }
    }

    // ReservationStation.java
    public void writeBack(CommonDataBus cdb, RegisterFile registerFile) {
        for (ReservationStationEntry entry : entries) {
            if (entry.isExecutionComplete() && !entry.isResultWritten()) {
                String destination = entry.getDestination();
                double result = entry.getResult();
                // Write the result to the register
                registerFile.setRegisterValue(destination, result);
                // Remove the specific Qi (reservation station name)
                registerFile.removeQi(destination, this.name);
                entry.setResultWritten(true);

                // Add entry to Common Data Bus
                CDBEntry cdbEntry = new CDBEntry(destination, result);
                cdb.addEntry(cdbEntry);
                System.out.println("Result written for: " + entry.getInstruction().getOpcode());
            }
        }
    }

    public void updateEntries(CDBEntry entry) {
        for (ReservationStationEntry rsEntry : entries) {
            if ("ADD.D".equals(rsEntry.getInstruction().getOpcode()) ||
                    "SUB.D".equals(rsEntry.getInstruction().getOpcode()) ||
                    "MUL.D".equals(rsEntry.getInstruction().getOpcode()) ||
                    "DIV.D".equals(rsEntry.getInstruction().getOpcode()) ||
                    "L.D".equals(rsEntry.getInstruction().getOpcode()) ||
                    "L.S".equals(rsEntry.getInstruction().getOpcode()) ||
                    "S.D".equals(rsEntry.getInstruction().getOpcode())||
                    "S.S".equals(rsEntry.getInstruction().getOpcode())||
                    "LD".equals(rsEntry.getInstruction().getOpcode())||
                    "LW".equals(rsEntry.getInstruction().getOpcode())||
                    "SW".equals(rsEntry.getInstruction().getOpcode())||
                    "DADDI".equals(rsEntry.getInstruction().getOpcode())||
                    "SUBI".equals(rsEntry.getInstruction().getOpcode())||
                    "DSUBI".equals(rsEntry.getInstruction().getOpcode())||
                    "SD".equals(rsEntry.getInstruction().getOpcode())||
                    "BNE".equals(rsEntry.getInstruction().getOpcode())||
                    "BEQ".equals(rsEntry.getInstruction().getOpcode())) {

                if (entry.getDestination().equals(rsEntry.getInstruction().getSource1())) {
                    rsEntry.setVj(entry.getResult());
                    rsEntry.setQj(null);
                }
                if (entry.getDestination().equals(rsEntry.getInstruction().getSource2())) {
                    rsEntry.setVk(entry.getResult());
                    rsEntry.setQk(null);
                }
                if("S.D".equals(rsEntry.getInstruction().getOpcode())){

                        if (entry.getDestination().equals(rsEntry.getInstruction().getDestination())) {
                            rsEntry.setVj(entry.getResult());
                            rsEntry.setQj(null);
                        }
                }
            }
        }
    }

    public void markResultWritten(String destination) {
        for (ReservationStationEntry entry : entries) {
            if (entry.getDestination() != null && entry.getDestination().equals(destination)) {
                entry.setResultWritten(true);
            }
        }
    }

    public void removeCompletedEntries() {
        Iterator<ReservationStationEntry> iterator = entries.iterator();
        while (iterator.hasNext()) {
            ReservationStationEntry entry = iterator.next();
            if (entry.isExecutionComplete() && entry.isResultWritten()) {
                iterator.remove();
                System.out.println("Removed completed instruction: " + entry.getInstruction().getOpcode() + " from " + this.name);
            }
        }
    }

    // For debugging purposes
    public void printStatus() {
        System.out.println("Reservation Station: " + name);
        for (ReservationStationEntry entry : entries) {
            System.out.println("  Instruction: " + entry.getInstruction().getOpcode() +
                    ", Remaining Cycles: " + entry.getRemainingCycles() +
                    ", Vj: " + entry.getVj() +
                    ", Vk: " + entry.getVk() +
                    ", Qj: " + entry.getQj() +
                    ", Qk: " + entry.getQk());
        }
    }
}