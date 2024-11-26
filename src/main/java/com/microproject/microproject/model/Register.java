// Java
package com.microproject.microproject.model;

public class Register {
    private String name;
    private double value;
    private String reservationStation;

    // Default constructor
    public Register() {
    }

    // Parameterized constructor
    public Register(String name, double value) {
        this.name = name;
        this.value = value;
        this.reservationStation = "";
    }

    // Getters and setters
    // ...
}