// Java
package com.microproject.microproject.model;

import java.util.ArrayList;

public class Register {

    private ArrayList<String> Qi;
    private String name;
    private double value;

    public Register() {
    }

    // Float Regiser File
    public Register(String name, double value, ArrayList<String> Qi) {
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


    public ArrayList<String> getQi() {
        return Qi;
    }

    public void setQi(ArrayList<String> qi) {
        Qi = qi;
    }

    public void removeFirstQi() {
        if (Qi != null && !Qi.isEmpty()) {
            Qi.removeFirst();
        }
    }
    // Register.java
    public void removeQi(String Qi) {
        if (this.Qi != null && !this.Qi.isEmpty()) {
            this.Qi.remove(Qi);
        }
    }
    public void addQi(String Qi) {
        this.Qi.add(Qi);
    }
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

}