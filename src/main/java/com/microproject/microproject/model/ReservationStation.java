// Java
package com.microproject.microproject.model;

public class ReservationStation {
    private String name;
    private boolean busy;
    private String operation;
    private String Vj;
    private String Vk;
    private String Qj;
    private String Qk;
    private int latency;
    private int address;

    // Default constructor
    public ReservationStation() {
    }

    // Parameterized constructor
    public ReservationStation(String name) {
        this.name = name;
        this.busy = false;
        this.operation = "";
        this.Vj = "";
        this.Vk = "";
        this.Qj = "";
        this.Qk = "";
        this.latency = 0;
        this.address = 0;
    }

    // Getters and setters
    // ...
}