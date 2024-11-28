package com.microproject.microproject.model;

public class InstructionSet {

    public static void DADDI(String destination, String source, int immediate) {
        Instruction instruction = new Instruction("DADDI", 1, destination, source, null);
        System.out.println(instruction.getOpcode() + " " + instruction.getDestination() + ", " + instruction.getSource1() + ", " + immediate);
    }

    public static void DSUBI(String destination, String source, int immediate) {
        Instruction instruction = new Instruction("DSUBI", 1, destination, source, null);
        System.out.println(instruction.getOpcode() + " " + instruction.getDestination() + ", " + instruction.getSource1() + ", " + immediate);
    }

    public static void ADD_D(String destination, String source1, String source2) {
        Instruction instruction = new Instruction("ADD.D", 1, destination, source1, source2);
        System.out.println(instruction.getOpcode() + " " + instruction.getDestination() + ", " + instruction.getSource1() + ", " + instruction.getSource2());
    }

    public static void ADD_S(String destination, String source1, String source2) {
        Instruction instruction = new Instruction("ADD.S", 1, destination, source1, source2);
        System.out.println(instruction.getOpcode() + " " + instruction.getDestination() + ", " + instruction.getSource1() + ", " + instruction.getSource2());
    }

    public static void SUB_D(String destination, String source1, String source2) {
        Instruction instruction = new Instruction("SUB.D", 1, destination, source1, source2);
        System.out.println(instruction.getOpcode() + " " + instruction.getDestination() + ", " + instruction.getSource1() + ", " + instruction.getSource2());
    }

    public static void SUB_S(String destination, String source1, String source2) {
        Instruction instruction = new Instruction("SUB.S", 1, destination, source1, source2);
        System.out.println(instruction.getOpcode() + " " + instruction.getDestination() + ", " + instruction.getSource1() + ", " + instruction.getSource2());
    }

    public static void MUL_D(String destination, String source1, String source2) {
        Instruction instruction = new Instruction("MUL.D", 1, destination, source1, source2);
        System.out.println(instruction.getOpcode() + " " + instruction.getDestination() + ", " + instruction.getSource1() + ", " + instruction.getSource2());
    }

    public static void MUL_S(String destination, String source1, String source2) {
        Instruction instruction = new Instruction("MUL.S", 1, destination, source1, source2);
        System.out.println(instruction.getOpcode() + " " + instruction.getDestination() + ", " + instruction.getSource1() + ", " + instruction.getSource2());
    }

    public static void DIV_D(String destination, String source1, String source2) {
        Instruction instruction = new Instruction("DIV.D", 1, destination, source1, source2);
        System.out.println(instruction.getOpcode() + " " + instruction.getDestination() + ", " + instruction.getSource1() + ", " + instruction.getSource2());
    }

    public static void DIV_S(String destination, String source1, String source2) {
        Instruction instruction = new Instruction("DIV.S", 1, destination, source1, source2);
        System.out.println(instruction.getOpcode() + " " + instruction.getDestination() + ", " + instruction.getSource1() + ", " + instruction.getSource2());
    }

    public static void LW(String destination, int offset, String base) {
        Instruction instruction = new Instruction("LW", 1, destination, base, null);
        System.out.println(instruction.getOpcode() + " " + instruction.getDestination() + ", " + offset + "(" + instruction.getSource1() + ")");
    }

    public static void LD(String destination, int offset, String base) {
        Instruction instruction = new Instruction("LD", 1, destination, base, null);
        System.out.println(instruction.getOpcode() + " " + instruction.getDestination() + ", " + offset + "(" + instruction.getSource1() + ")");
    }

    public static void L_S(String destination, int offset, String base) {
        Instruction instruction = new Instruction("L.S", 1, destination, base, null);
        System.out.println(instruction.getOpcode() + " " + instruction.getDestination() + ", " + offset + "(" + instruction.getSource1() + ")");
    }

    public static void L_D(String destination, int offset, String base) {
        Instruction instruction = new Instruction("L.D", 1, destination, base, null);
        System.out.println(instruction.getOpcode() + " " + instruction.getDestination() + ", " + offset + "(" + instruction.getSource1() + ")");
    }

    public static void SW(String source, int offset, String base) {
        Instruction instruction = new Instruction("SW", 1, source, base, null);
        System.out.println(instruction.getOpcode() + " " + instruction.getDestination() + ", " + offset + "(" + instruction.getSource1() + ")");
    }

    public static void SD(String source, int offset, String base) {
        Instruction instruction = new Instruction("SD", 1, source, base, null);
        System.out.println(instruction.getOpcode() + " " + instruction.getDestination() + ", " + offset + "(" + instruction.getSource1() + ")");
    }

    public static void S_S(String source, int offset, String base) {
        Instruction instruction = new Instruction("S.S", 1, source, base, null);
        System.out.println(instruction.getOpcode() + " " + instruction.getDestination() + ", " + offset + "(" + instruction.getSource1() + ")");
    }

    public static void S_D(String source, int offset, String base) {
        Instruction instruction = new Instruction("S.D", 1, source, base, null);
        System.out.println(instruction.getOpcode() + " " + instruction.getDestination() + ", " + offset + "(" + instruction.getSource1() + ")");
    }

    public static void BNE(String source1, String source2, String label) {
        Instruction instruction = new Instruction("BNE", 1, null, source1, source2);
        System.out.println(instruction.getOpcode() + " " + instruction.getSource1() + ", " + instruction.getSource2() + ", " + label);
    }

    public static void BEQ(String source1, String source2, String label) {
        Instruction instruction = new Instruction("BEQ", 1, null, source1, source2);
        System.out.println(instruction.getOpcode() + " " + instruction.getSource1() + ", " + instruction.getSource2() + ", " + label);
    }
}