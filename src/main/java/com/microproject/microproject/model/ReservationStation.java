// Java
package com.microproject.microproject.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    public void issue(Instruction inst, RegisterFile registerFile, int latency) {
        // Create a new entry
        ReservationStationEntry entry = new ReservationStationEntry(inst, latency);
        // Set operands and Qj/Qk based on register status
        entry.setupOperands(registerFile);
        entries.add(entry);
        System.out.println("Issued instruction: " + inst.getOpcode() + " to " + name);
    }

    public void execute(CommonDataBus cdb) {
        for (ReservationStationEntry entry : entries) {
            if (entry.isReady()) {
                entry.execute();
            }
        }
        // Remove entries that have finished execution
        Iterator<ReservationStationEntry> iterator = entries.iterator();
        while (iterator.hasNext()) {
            ReservationStationEntry entry = iterator.next();
            if (entry.isExecutionComplete()) {
                cdb.addResult(entry.getDestination(), entry.getResult());
                iterator.remove();
            }
        }
    }
}