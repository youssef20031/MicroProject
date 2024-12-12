// src/main/java/com/microproject/microproject/model/RegisterStatus.java
package com.microproject.microproject.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RegisterStatus {
    private final StringProperty registerName;
    private final StringProperty registerValue;

    public RegisterStatus(String registerName, String registerValue) {
        this.registerName = new SimpleStringProperty(registerName);
        this.registerValue = new SimpleStringProperty(registerValue);
    }

    public String getRegisterName() {
        return registerName.get();
    }

    public void setRegisterName(String registerName) {
        this.registerName.set(registerName);
    }

    public StringProperty registerNameProperty() {
        return registerName;
    }

    public String getRegisterValue() {
        return registerValue.get();
    }

    public void setRegisterValue(String registerValue) {
        this.registerValue.set(registerValue);
    }

    public StringProperty registerValueProperty() {
        return registerValue;
    }
}