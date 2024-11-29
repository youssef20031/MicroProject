// Java
package com.microproject.microproject.model;

import java.util.ArrayList;
import java.util.List;

public class CommonDataBus {
    private List<CDBEntry> entries;

    public CommonDataBus() {
        this.entries = new ArrayList<>();
    }

    public void addResult(String destination, double result) {
        entries.add(new CDBEntry(destination, result));
    }

    public void broadcast() {
        for (CDBEntry entry : entries) {
            // Update registers and waiting reservation stations
            System.out.println("Broadcasting result for " + entry.getDestination());
            // Implement the logic to update registers and reservation stations
        }
        entries.clear();
    }
}

class CDBEntry {
    private String destination;
    private double result;

    public CDBEntry(String destination, double result) {
        this.destination = destination;
        this.result = result;
    }

    public String getDestination() {
        return destination;
    }

    public double getResult() {
        return result;
    }
}