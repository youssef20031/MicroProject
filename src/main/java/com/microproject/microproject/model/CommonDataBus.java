package com.microproject.microproject.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommonDataBus {
    private List<CDBEntry> entries;

    public CommonDataBus() {
        this.entries = new ArrayList<>();
    }

    public void addResult(String destination, double result) {
        entries.add(new CDBEntry(destination, result));
    }

    public void broadcast(List<ReservationStation> reservationStations, RegisterFile registerFile, Map<String, String> registerStatus) {
        for (CDBEntry entry : entries) {
            // Update register file
            registerFile.setRegisterValue(entry.getDestination(), entry.getResult());

            // Clear register status
            registerStatus.remove(entry.getDestination());

            // Update waiting reservation stations
            for (ReservationStation rs : reservationStations) {
                rs.updateEntries(entry);
            }

            System.out.println("Broadcasting result for " + entry.getDestination());
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