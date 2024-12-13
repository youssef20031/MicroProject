package com.microproject.microproject.model;

public class Pair{
    String reservationStationName;
    int instructionNumber;

    public Pair(String reservationStationName, int instructionNumber){
        this.reservationStationName = reservationStationName;
        this.instructionNumber = instructionNumber;
    }

    public String getReservationStationName() {
        return reservationStationName;
    }
    public int getInstructionNumber() {
        return instructionNumber;
    }
}