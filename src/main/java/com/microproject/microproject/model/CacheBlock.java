// Java
package com.microproject.microproject.model;

public class CacheBlock {
    private int address;
    private double data;
    private boolean valid;
    private boolean dirty;

    // Default constructor
    public CacheBlock() {
    }

    // Parameterized constructor
    public CacheBlock(int address, double data) {
        this.address = address;
        this.data = data;
        this.valid = true;
        this.dirty = false;
    }

    // Getters and setters
    // ...
}