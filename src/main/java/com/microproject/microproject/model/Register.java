// Java
package com.microproject.microproject.model;

public class Register {

    private String Qi;
    private String name;
    private double value;

    public Register() {
    }

    // Float Regiser File
    public Register(String name, double value, String Qi) {
        this.name = name;
        this.value = value;
        this.Qi = Qi;

    }
    // Integer Regiser File
    public Register(String name, double value) {
        this.name = name;
        this.value = value;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getQi() {
        return Qi;
    }

    public void setQi(String qi) {
        Qi = qi;
    }


    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }



    // Default constructor

    // Getters and setters
    // ...
}