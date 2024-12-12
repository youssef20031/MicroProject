
package com.microproject.microproject.model;

public class CacheEntry {
    private int address;
    private double data;

    public CacheEntry(int address, double data) {
        this.address = address;
        this.data = data;
    }

    public int getAddress() {
        return address;
    }

    public double getData() {
        return data;
    }
}