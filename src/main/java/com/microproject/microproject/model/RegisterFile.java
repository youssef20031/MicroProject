package com.microproject.microproject.model;

import java.util.ArrayList;

public class RegisterFile {
    private final Register[] floatRegisterFile;
    private final Register[] integerRegisterFile;

    public RegisterFile() {
        this.floatRegisterFile = new Register[32];
        for (int i = 0; i < 32; i++) {
            floatRegisterFile[i] = new Register("F" + i, 0, new ArrayList<ArrayList<String>>());
        }
        this.integerRegisterFile = new Register[32];
        for (int i = 0; i < 32; i++) {
            integerRegisterFile[i] = new Register("R" + i, 0, new ArrayList<ArrayList<String>>());
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
        // name = R11, value = 14
        if (name.startsWith("F")) {
            int index = Integer.parseInt(name.substring(1));
            floatRegisterFile[index].setValue(value);
            //floatRegisterFile[index].removeFirstQi(); // Clear Qi after write-back
        } else if (name.startsWith("R")) {
            int index = Integer.parseInt(name.substring(1));
            integerRegisterFile[index].setValue(value);
            //integerRegisterFile[index].removeFirstQi(); // Clear Qi after write-back
        }
    }

    // Set Qi for a register
    public void setRegisterQi(String name, ArrayList<String> Qi) {
        if (name.startsWith("F")) {
            int index = Integer.parseInt(name.substring(1));
            floatRegisterFile[index].addQi(Qi);
        } else if (name.startsWith("R")) {
            int index = Integer.parseInt(name.substring(1));
            integerRegisterFile[index].addQi(Qi);
        }
    }

    public void removeFirstQi(String name) {
        if (name.startsWith("F")) {
            int index = Integer.parseInt(name.substring(1));
            floatRegisterFile[index].removeFirstQi();
        } else if (name.startsWith("R")) {
            int index = Integer.parseInt(name.substring(1));
            integerRegisterFile[index].removeFirstQi();
        }
    }

    // Get Qi for a register
    public ArrayList<ArrayList<String>> getRegisterQi(String name) {
        if (name.startsWith("F")) {
            int index = Integer.parseInt(name.substring(1));
            return floatRegisterFile[index].getQi();
        } else if (name.startsWith("R")) {
            int index = Integer.parseInt(name.substring(1));
            return integerRegisterFile[index].getQi();
        }
        return null;
    }

    public void printStatus() {
        System.out.println("Register Status:");
        for (Register reg : floatRegisterFile) {
            System.out.println("  " + reg.getName() + ": " + reg.getValue() + " (Qi: " + reg.getQi() + ")");
        }
        for (Register reg : integerRegisterFile) {
            System.out.println("  " + reg.getName() + ": " + reg.getValue() + " (Qi: " + reg.getQi() + ")");
        }
    }

    public Register getRegister(String name) {
        if (name.startsWith("F")) {
            int index = Integer.parseInt(name.substring(1));
            return floatRegisterFile[index];
        } else if (name.startsWith("R")) {
            int index = Integer.parseInt(name.substring(1));
            return integerRegisterFile[index];
        }
        return null;
    }
    // RegisterFile.java
    public void removeQi(String name, ArrayList<String> Qi) {
        if (name.startsWith("F")) {
            int index = Integer.parseInt(name.substring(1));
            floatRegisterFile[index].removeQi(Qi);
        } else if (name.startsWith("R")) {
            int index = Integer.parseInt(name.substring(1));
            integerRegisterFile[index].removeQi(Qi);
        }
    }
}