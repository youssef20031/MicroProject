package com.microproject.microproject.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommonDataBus {
    private List<CDBEntry> entries;

    public CommonDataBus() {
        this.entries = new ArrayList<>();
    }



    public void broadcast(List<ReservationStation> reservationStations, RegisterFile registerFile, Map<String, String> registerStatus) {
        for (CDBEntry entry : entries) {
            if (entry.getDestination() != null) {
                // Update register file
                //registerFile.setRegisterValue(entry.getDestination(), entry.getResult());
                // Clear register status
                registerStatus.remove(entry.getDestination());

                // Update waiting reservation stations
                for (ReservationStation rs : reservationStations) {
                    rs.updateEntries(entry);
                }

                System.out.println("Broadcasting result for " + entry.getDestination());
            }
            if (entry.getSrc2() != null) {
                // Update waiting reservation stations for src2 dependency
                registerFile.setRegisterValue(entry.getSrc2(), registerFile.getRegisterValue(entry.getSrc2()));

                registerStatus.remove(entry.getSrc2());

                for (ReservationStation rs : reservationStations) {
                    rs.updateEntries(entry);
                }
                System.out.println("Broadcasting src2 completion for " + entry.getSrc2());
            }
        }
        entries.clear();
    }
    public void addEntry(CDBEntry entry) {
        entries.add(entry);
    }
}

