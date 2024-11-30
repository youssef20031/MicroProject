package com.microproject.microproject.model;

public class CDBEntry {
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
