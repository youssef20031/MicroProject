package com.microproject.microproject.model;

public class RegisterFile {
    private Register[] floatRegisterFile;
    private Register[] integerRegisterFile;

    public RegisterFile() {
        this.floatRegisterFile = new Register[32];
        for (int i = 0; i < 32; i++) {
            floatRegisterFile[i] = new Register("F" + i, 0, "");
        }
        this.integerRegisterFile = new Register[32];
        for (int i = 0; i < 32; i++) {
            integerRegisterFile[i] = new Register("R" + i, 0, "");
        }
    }

    public Register[] getFloatRegisterFile() {
        return floatRegisterFile;
    }

    public Register[] getIntegerRegisterFile() {
        return integerRegisterFile;
    }

    // Get register value by name
    public double getRegisterValue(String name) {
        if (name.startsWith("F")) {
            int index = Integer.parseInt(name.substring(1));
            return floatRegisterFile[index].getValue();
        } else if (name.startsWith("R")) {
            int index = Integer.parseInt(name.substring(1));
            return integerRegisterFile[index].getValue();
        }
        return 0;
    }

    // Set register value by name
    public void setRegisterValue(String name, double value) {
        if (name.startsWith("F")) {
            int index = Integer.parseInt(name.substring(1));
            floatRegisterFile[index].setValue(value);
            floatRegisterFile[index].setQi(""); // Clear Qi after write-back
        } else if (name.startsWith("R")) {
            int index = Integer.parseInt(name.substring(1));
            integerRegisterFile[index].setValue(value);
            integerRegisterFile[index].setQi("");
        }
    }

    // Set Qi for a register
    public void setRegisterQi(String name, String Qi) {
        if (name.startsWith("F")) {
            int index = Integer.parseInt(name.substring(1));
            floatRegisterFile[index].setQi(Qi);
        } else if (name.startsWith("R")) {
            int index = Integer.parseInt(name.substring(1));
            integerRegisterFile[index].setQi(Qi);
        }
    }

    // Get Qi for a register
    public String getRegisterQi(String name) {
        if (name.startsWith("F")) {
            int index = Integer.parseInt(name.substring(1));
            return floatRegisterFile[index].getQi();
        } else if (name.startsWith("R")) {
            int index = Integer.parseInt(name.substring(1));
            return integerRegisterFile[index].getQi();
        }
        return "";
    }
}