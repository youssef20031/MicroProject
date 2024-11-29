package com.microproject.microproject.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ReservationStation {
    private int capacity;
    private String name;
    private List<ReservationStationEntry> entries;

    public ReservationStation(int capacity, String name) {
        this.capacity = capacity;
        this.name = name;
        this.entries = new ArrayList<>();
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
        ReservationStationEntry entry = new ReservationStationEntry(inst, latency);

        // Get source operands
        String src1 = inst.getSource1();
        String src2 = inst.getSource2();

        // Check source 1
        if (registerStatus.containsKey(src1)) {
            entry.setQj(registerStatus.get(src1));
        } else {
            entry.setVj(registerFile.getRegisterValue(src1));
        }

        // Check source 2
        if (registerStatus.containsKey(src2)) {
            entry.setQk(registerStatus.get(src2));
        } else {
            entry.setVk(registerFile.getRegisterValue(src2));
        }

        // Update register status for destination
        String dest = inst.getDestination();
        if (dest != null && !inst.getOpcode().startsWith("S.")) { // Store instructions do not write to registers
            registerStatus.put(dest, this.name);
        }

        entries.add(entry);
        System.out.println("Issued instruction: " + inst.getOpcode() + " to " + this.name);
    }

    public void execute() {
        for (ReservationStationEntry entry : entries) {
            entry.execute();
        }
    }

    public void writeBack(CommonDataBus cdb) {
        for (ReservationStationEntry entry : entries) {
            if (entry.isExecutionComplete() && !entry.isResultWritten()) {
                cdb.addResult(entry.getDestination(), entry.getResult());
                entry.setResultWritten(true);
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
                "S.D".equals(rsEntry.getInstruction().getOpcode())) {
                
                if (entry.getDestination().equals(rsEntry.getInstruction().getSource1())) {
                    rsEntry.setVj(entry.getResult());
                    rsEntry.setQj(null);
                }
                if (entry.getDestination().equals(rsEntry.getInstruction().getSource2())) {
                    rsEntry.setVk(entry.getResult());
                    rsEntry.setQk(null);
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