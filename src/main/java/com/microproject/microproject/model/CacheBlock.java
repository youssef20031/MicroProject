// Java
package com.microproject.microproject.model;

public class CacheBlock {
    private int address;
    private Register[] data;
    private boolean valid;
    private boolean dirty;
    private int blockSize;

    // Default constructor
    public CacheBlock() {
    }

    // Parameterized constructor
    public CacheBlock(int address, int blockSize) {
        this.address = address;
        this.data = new Register[blockSize];
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

    public Register[] getData() {
        return data;
    }

    public void setData(Register[] data) {
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