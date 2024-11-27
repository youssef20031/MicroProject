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

    // Reservation Station
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

    // Load Buffer
    public ReservationStation(boolean busy, int address) {
        this.busy = busy;
        this.address = address;
        this.operation = "";
        this.Vj = "";
        this.Vk = "";
        this.Qj = "";
        this.Qk = "";
        this.latency = 0;
    }

    // Store Buffer
    public ReservationStation(boolean busy, int address, String V, String Q) {
        this.busy = busy;
        this.address = address;
        this.Vj = Vj;
        this.Vk = Vk;
        this.Qj = Qj;
        this.Qk = Qk;
        this.operation = "";
        this.latency = 0;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getVj() {
        return Vj;
    }

    public void setVj(String vj) {
        Vj = vj;
    }

    public String getVk() {
        return Vk;
    }

    public void setVk(String vk) {
        Vk = vk;
    }

    public String getQj() {
        return Qj;
    }

    public void setQj(String qj) {
        Qj = qj;
    }

    public String getQk() {
        return Qk;
    }

    public void setQk(String qk) {
        Qk = qk;
    }

    public int getLatency() {
        return latency;
    }

    public void setLatency(int latency) {
        this.latency = latency;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }
}