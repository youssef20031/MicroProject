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
        ReservationStationEntry entry = new ReservationStationEntry(inst, latency, this.name);

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
        if (dest != null && !inst.getOpcode().startsWith("S.")) {
            registerStatus.put(dest, this.name);
        }

        entries.add(entry);
    }

    public void execute(CommonDataBus cdb) {
        for (ReservationStationEntry entry : entries) {
            entry.execute(cdb);
        }

        // Remove entries that have finished execution
        Iterator<ReservationStationEntry> iterator = entries.iterator();
        while (iterator.hasNext()) {
            ReservationStationEntry entry = iterator.next();
            if (entry.isExecutionComplete()) {
                iterator.remove();
            }
        }
    }

    // Update entries when data is broadcasted on the CDB
    public void updateEntries(CDBEntry entry) {
        for (ReservationStationEntry rsEntry : entries) {
            if (entry.getDestination().equals(rsEntry.getQj())) {
                rsEntry.setVj(entry.getResult());
                rsEntry.setQj(null);
            }
            if (entry.getDestination().equals(rsEntry.getQk())) {
                rsEntry.setVk(entry.getResult());
                rsEntry.setQk(null);
            }
        }
    }
}