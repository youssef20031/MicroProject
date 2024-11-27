// Java
package com.microproject.microproject.model;

public class CacheBlock {
    private int address;
    private double[] data;
    private boolean valid;
    private boolean dirty;

    // Default constructor
    public CacheBlock() {
    }

    // Parameterized constructor
    public CacheBlock(int address, int blockSize) {
        this.address = address;
        this.data = new double[blockSize];
        this.valid = true;
        this.dirty = false;
    }

    // Getters and setters
    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public double[] getData() {
        return data;
    }

    public void setData(double[] data) {
        this.data = data;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }
}