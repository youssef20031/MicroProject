package com.microproject.microproject.model;

public class CDBEntry {
    private String destination;
    private double result;
    private String src2;

    // Constructor for instructions that have a destination register
    public CDBEntry(String destination, double result, String src2) {
        this.destination = destination;
        this.result = result;
        this.src2 = src2;
    }

    // Overloaded constructor for instructions like S.D that need to broadcast src2
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

    public String getSrc2() {
        return src2;
    }
}
