// Java
package com.microproject.microproject.model;

import java.util.ArrayList;

public class Register {

    private ArrayList<ArrayList<String>> Qi;
    private String name;
    private double value;
    private int instructionNumber;

    public Register() {
    }

    // Float Regiser File
    public Register(String name, double value, ArrayList<ArrayList<String>> Qi) {
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


    public ArrayList<ArrayList<String>> getQi() {
        return Qi;
    }

    public void setQi(ArrayList<ArrayList<String>> qi) {
        Qi = qi;
    }

    public void removeFirstQi() {
        if (Qi != null && !Qi.isEmpty()) {
            Qi.removeFirst();
        }
    }
    // Register.java
    public void removeQi(ArrayList<String> Qi) {
        if (this.Qi != null && Qi != null) {
            for(int i = 0;i < this.Qi.size();i++) {
                if(this.Qi.get(i).getFirst().equals(Qi.getFirst()) && this.Qi.get(i).getLast().equals(Qi.getLast())) {
                    this.Qi.remove(i);
                    break;
                }
            }

        }
    }
    public void addQi(ArrayList<String> Qi) {
        if(this.Qi == null) {
            this.Qi = new ArrayList<>();
        }
        if(Qi != null)
            this.Qi.add(Qi);
    }
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

}