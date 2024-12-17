package com.microproject.microproject.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import static com.microproject.microproject.Controller.Main.registerFile;
import static com.microproject.microproject.simulator.TomasuloSimulator.isBranchInProgress;

public class ReservationStation {
    private final int capacity;
    private final String name;
    private final List<ReservationStationEntry> entries;

    public ReservationStation(int capacity, String name) {
        this.capacity = capacity;
        this.name = name;
        this.entries = new ArrayList<>();
    }

    public List<ReservationStationEntry> getEntries() {
        return entries;
    }

    public String getName() {
        return name;
    }

    public boolean isFull() {
        return entries.size() >= capacity;
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    public void issue(Instruction inst, RegisterFile registerFile, Map<String, String> registerStatus, int latency) {
        ReservationStationEntry entry = new ReservationStationEntry(this.name,inst, latency);

        // Set the actual destination register name
        String opcode = inst.getOpcode();
        String destination = inst.getDestination();
        entry.setDestination(destination);
        entry.setOperation(inst.getOpcode());

        // Get source registers
        String src1 = inst.getSource1();
        String src2 = inst.getSource2();
        String dest = destination;

        // LD F0, 0, R1
        if (opcode.equals("L.D") || opcode.equals("L.S") || opcode.equals("LD") || opcode.equals("LW")) {
            // Check Qi from registerFile(check if R1 is busy or not)
            if (src2 != null && !src2.isEmpty()) {
                ArrayList<ArrayList<String>> src2Qi = registerFile.getRegisterQi(src2);
                if (src2Qi != null && !src2Qi.isEmpty()) {
                    entry.addQk(new Pair(src2Qi.getLast().getFirst(), Integer.parseInt(src2Qi.getLast().getLast())));
                } else {
                    double baseValue = registerFile.getRegisterValue(src2);

                    double offset = Double.parseDouble(src1);
                    double effectiveAddress = baseValue + offset;
                    if(effectiveAddress % 1 != 0){
                        throw new NumberFormatException("Invalid number format for register value: " + src1);
                    }
                    entry.setVk(effectiveAddress);
                    entry.getInstruction().setEffectiveAddress((int) effectiveAddress);
                }
            }
        }
        // S.D F4, 0, R1
        else if (opcode.equals("S.D") || opcode.equals("S.S") || opcode.equals("SD") || opcode.equals("SW")) {
            // For Store: Compute effective address Vk = base register value + offset
            // Check Qi from registerFile(check if R1 is busy or not)
            if (src2 != null && !src2.isEmpty()) {
                ArrayList<ArrayList<String>> src2Qi = registerFile.getRegisterQi(src2);
                System.out.println("src2Qi: " + src2Qi);
                if (src2Qi != null && !src2Qi.isEmpty()) {
                    entry.addQk(new Pair(src2Qi.getLast().getFirst(), Integer.parseInt(src2Qi.getLast().getLast())));
                } else {
                    double baseValue = registerFile.getRegisterValue(src2);
                    double offset = Double.parseDouble(src1);
                    double effectiveAddress = baseValue + offset;
                    if(effectiveAddress % 1 != 0){
                        throw new NumberFormatException("Invalid number format for register value: " + src1);
                    }
                    entry.setVk(effectiveAddress);
//                    registerFile.setRegisterQi(src2, this.name);
                }
            }

            // Check Qi from registerFile(check if F4 is busy or not)
            if (registerFile.getRegisterQi(dest) != null && !registerFile.getRegisterQi(dest).isEmpty()) {
                ArrayList<ArrayList<String>> destQi = registerFile.getRegisterQi(dest);
                if (destQi != null && !destQi.isEmpty()) {
                    entry.addQj(new Pair(destQi.getLast().getFirst(), Integer.parseInt(destQi.getLast().getLast())));
                }
            } else {
                entry.setVj(registerFile.getRegisterValue(dest));
            }
        }// BNE R1, R2, 2
        else if (opcode.equals("BNE") || opcode.equals("BEQ")) {
            isBranchInProgress = true;
            // Check Qi from registerFile(check if R1 is busy or not)
            if (destination != null && !destination.isEmpty()) {
                ArrayList<ArrayList<String>> destQi = registerFile.getRegisterQi(destination);
                if (destQi != null && !destQi.isEmpty()) {
                    entry.addQj(new Pair(destQi.get(destQi.size() - 1).getFirst(), Integer.parseInt(destQi.get(destQi.size() - 1).get(1))));
                } else {
                    entry.setVj(registerFile.getRegisterValue(destination));
                }
            }

            // Check Qi from registerFile(check if R2 is busy or not)
            if (src1 != null && !src1.isEmpty()) {
                ArrayList<ArrayList<String>> src1Qi = registerFile.getRegisterQi(src1);
                if (src1Qi != null && !src1Qi.isEmpty()) {
                    entry.addQk(new Pair(src1Qi.get(src1Qi.size() - 1).getFirst(), Integer.parseInt(src1Qi.get(src1Qi.size() - 1).get(1))));
                } else {
                    entry.setVk(registerFile.getRegisterValue(src1));
                }
            }
            // if src2 is a floating point number, throw an exception
            if (src2.contains(".")) {
                throw new NumberFormatException("Invalid number format for register value: " + src1);
            }
            // The branch target (src2) is an immediate value (offset), so store it
            entry.setImmediate(Integer.parseInt(src2));
        }
        // any other ALU instructions
        // ex. ADD.D F0, F2, F4
        else {
            // Check Qi from registerFile(check if F0 is busy or not)
            if (destination != null && !destination.isEmpty()) {
                ArrayList<ArrayList<String>> destQi = registerFile.getRegisterQi(destination);
                if (destQi != null && !destQi.isEmpty()) {
                    entry.addQd(new Pair(destQi.get(destQi.size() - 1).getFirst(), Integer.parseInt(destQi.get(destQi.size() - 1).get(1))));
                }
            }

            // Check Qi from registerFile(check if F2 is busy or not)
            if (src1 != null && !src1.isEmpty()) {
                ArrayList<ArrayList<String>> src1Qi = registerFile.getRegisterQi(src1);
                if (src1Qi != null && !src1Qi.isEmpty()) {
                    entry.addQj(new Pair(src1Qi.getLast().getFirst(), Integer.parseInt(src1Qi.getLast().getLast())));
                } else {
                    entry.setVj(registerFile.getRegisterValue(src1));
                }
            }

            // Check Qi for source2 from registerFile(check if F4 is busy or not)
            if (src2 != null && !src2.isEmpty()) {
                ArrayList<ArrayList<String>> src2Qi = registerFile.getRegisterQi(src2);
                if (src2Qi != null && !src2Qi.isEmpty()) {
                    entry.addQk(new Pair(src2Qi.getLast().get(0), Integer.parseInt(src2Qi.getLast().getLast())));
                } else {
                    if (src2.charAt(0) != 'F' && src2.charAt(0) != 'R')
                        entry.setVk(Double.parseDouble(src2));
                    else
                        entry.setVk(registerFile.getRegisterValue(src2));
                }
            }
        }
        // if Branch or Store instruction, don't set Qi, because they don't have a destination register to store into
        if (!opcode.equals("BNE") && !opcode.equals("BEQ") && !opcode.equals("S.D") && !opcode.equals("S.S") && !opcode.equals("SD") && !opcode.equals("SW")) {
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(this.name);
            arrayList.add(inst.getInstructionNumber() + "");
            registerFile.setRegisterQi(destination, arrayList);
        }
        entries.add(entry);
    }

    // execute reservation station entry
    public void execute(RegisterFile registerFile) {
        for (ReservationStationEntry entry : entries) {
            entry.execute(registerFile);
        }
    }

    public void writeBack(CommonDataBus cdb, RegisterFile registerFile) {
        for (ReservationStationEntry entry : entries) {
            if (entry.isExecutionComplete() && !entry.isResultWritten()) {
                String opcode = entry.getInstruction().getOpcode();
                String destination = entry.getDestination();
                double result = entry.getResult();
                // S.D F4, 0, R1, store value of F4 in address 0 + R1 in Cache
                if (opcode.equals("S.D") || opcode.equals("S.S") || opcode.equals("SD") || opcode.equals("SW")) {
                    // For store instructions, broadcast src2 to clear dependencies
                    String src2 = entry.getInstruction().getSource2();
                    entry.setResultWritten(true);
                    // Create a CDBEntry with src2
                    CDBEntry cdbEntry = new CDBEntry(entry.getInstruction(), destination, result, src2, entry.getInstruction().getInstructionNumber());
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.add(this.name);
                    arrayList.add(entry.getInstruction().getInstructionNumber() + "");
                    registerFile.removeQi(entry.getInstruction().getSource2(), arrayList);
                    registerFile.removeQi(destination, arrayList);
                    cdb.addEntry(cdbEntry);
                    System.out.println("Store instruction completed: " + opcode + ". Broadcasting src2: " + src2);
                } else {
                    // Write the result to the register
                    registerFile.setRegisterValue(destination, result);
                    // Remove the specific Qi (reservation station name)
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.add(this.name);
                    arrayList.add(entry.getInstruction().getInstructionNumber() + "");
                    registerFile.removeQi(destination, arrayList);
                    // Add entry to Common Data Bus
                    CDBEntry cdbEntry = new CDBEntry(entry.getInstruction(), destination, result, entry.getInstruction().getInstructionNumber());
                    cdb.addEntry(cdbEntry);
                    System.out.println("Result written for: " + opcode);
                }

                entry.setResultWritten(true);
            }
        }
        removeCompletedEntries();

    }
    // after broadcasting to CDB, update the reservation station entries(Qd, Qj, Qk)
    public void updateEntries(CDBEntry entry) {
        for (ReservationStationEntry rsEntry : entries) {
            // loop on getQd()
            if(rsEntry.getQd() != null && !rsEntry.getQd().isEmpty()) {
                for (int i = 0; i < rsEntry.getQd().size(); i++) {
                    // (entry) will broadcast fe (rsEntry) eli entry kanet me2a5raha
                    if (entry.getInstructionNumber() == rsEntry.getQd().get(i).instructionNumber) {
                        // bafadeh Qd
                        rsEntry.setQd(new ArrayList<>());
                    }
                }
            }
            // loop on getQj()
            if(rsEntry.getQj() != null && !rsEntry.getQj().isEmpty()) {
                for (int i = 0; i < rsEntry.getQj().size(); i++) {
                    if (entry.getInstructionNumber() == rsEntry.getQj().get(i).instructionNumber) {
                        if (entry.getDestination().equals(rsEntry.getInstruction().getSource1())) {
                            rsEntry.setVj(entry.getResult());
                            rsEntry.setQj(new ArrayList<>());
                        }
                        if ("S.D".equals(rsEntry.getInstruction().getOpcode()) ||
                                "S.S".equals(rsEntry.getInstruction().getOpcode()) ||
                                "SD".equals(rsEntry.getInstruction().getOpcode()) ||
                                "SW".equals(rsEntry.getInstruction().getOpcode())) {

                            if (entry.getDestination().equals(rsEntry.getInstruction().getDestination())) {
                                rsEntry.setVj(entry.getResult());
                                rsEntry.setQj(new ArrayList<>());
                            }
                        }
                        if ("BNE".equals(rsEntry.getInstruction().getOpcode()) || "BEQ".equals(rsEntry.getInstruction().getOpcode())) {
                            if (entry.getDestination().equals(rsEntry.getInstruction().getDestination())) {
                                rsEntry.setVj(entry.getResult());
                                rsEntry.setQj(new ArrayList<>());
                            }
                        }
                    }
                }
            }

            // loop on getQk()
            for(int i = 0; i < rsEntry.getQk().size();i++){
                if(entry.getInstructionNumber() == rsEntry.getQk().get(i).instructionNumber) {
                    if (entry.getDestination().equals(rsEntry.getInstruction().getSource2())) {
                        rsEntry.setVk(entry.getResult());
                        rsEntry.getInstruction().setEffectiveAddress((int) entry.getResult());
                        rsEntry.setQk(new ArrayList<>());
                    }
                    if ("S.D".equals(rsEntry.getInstruction().getOpcode()) ||
                            "S.S".equals(rsEntry.getInstruction().getOpcode()) ||
                            "SD".equals(rsEntry.getInstruction().getOpcode()) ||
                            "SW".equals(rsEntry.getInstruction().getOpcode())) {

                        if (entry.getDestination().equals(rsEntry.getInstruction().getDestination())) {
                            rsEntry.setVj(entry.getResult());
                            rsEntry.setQj(new ArrayList<>());
                        }
                        if (entry.getSrc2() != null && entry.getSrc2().equals(rsEntry.getInstruction().getSource2())) {
                            rsEntry.setVk(registerFile.getRegisterValue(entry.getSrc2()));
                            rsEntry.setQk(new ArrayList<>());
                        }
                    }
                }
            }
        }
    }

    public void removeCompletedEntries() {
        Iterator<ReservationStationEntry> iterator = entries.iterator();
        while (iterator.hasNext()) {
            ReservationStationEntry entry = iterator.next();
            if (entry.isExecutionComplete() && entry.isResultWritten()) {
                iterator.remove();
                System.out.println("Removed completed instruction: " + entry.getInstruction().getOpcode() + " from " + this.name);
            }
        }
    }

    // For debugging purposes
    public void printStatus() {
        System.out.println("Reservation Station: " + name);
        for (ReservationStationEntry entry : entries) {
            System.out.println("  Instruction: " + entry.getInstruction().getOpcode() +
                    ", Remaining Cycles: " + entry.getRemainingCycles() +
                    ", Vj: " + entry.getVj() +
                    ", Vk: " + entry.getVk() +
                    ", Qj: " + entry.getQj() +
                    ", Qk: " + entry.getQk());
        }
    }
}