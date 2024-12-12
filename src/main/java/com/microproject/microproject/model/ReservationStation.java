package com.microproject.microproject.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.microproject.microproject.Controller.Main.registerFile;


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
        // Create a new entry
        ReservationStationEntry entry = new ReservationStationEntry(inst, latency);

        // Set the actual destination register name
        String opcode = inst.getOpcode();
        String destination = inst.getDestination();
        entry.setDestination(destination);

        // Get source registers
        String src1 = inst.getSource1();
        String src2 = inst.getSource2();
        String dest = destination;


        if (opcode.equals("L.D") || opcode.equals("L.S") || opcode.equals("LD") || opcode.equals("LW")) {

            if (src2 != null && !src2.isEmpty()) {
                ArrayList<ArrayList<String>> src2Qi = registerFile.getRegisterQi(src2);
                if (src2Qi != null && !src2Qi.isEmpty()) {
                    entry.addQk(new ReservationStationEntry.Pair(src2Qi.getLast().getFirst(), inst.getInstructionNumber()));
                } else {
                    double baseValue = registerFile.getRegisterValue(src2);
                    int offset = Integer.parseInt(src1);
                    double effectiveAddress = baseValue + offset;
                    entry.setVk(effectiveAddress);
                    entry.getInstruction().setEffectiveAddress((int) effectiveAddress);
                }
            }

            // Destination register Qi is set to this reservation station
            //registerFile.setRegisterQi(dest, this.name);
        } else if (opcode.equals("S.D") || opcode.equals("S.S") || opcode.equals("SD") || opcode.equals("SW")) {
            // For Store: Compute effective address Vk = base register value + offset
            // Check Qi for source2 from registerFile
            if (src2 != null && !src2.isEmpty()) {
                ArrayList<ArrayList<String>> src2Qi = registerFile.getRegisterQi(src2);
                System.out.println("src2Qi: " + src2Qi);
                if (src2Qi != null && !src2Qi.isEmpty()) {
                    entry.addQk(new ReservationStationEntry.Pair(src2Qi.getLast().getFirst(), Integer.parseInt(src2Qi.getLast().getLast())));
                } else {
                    double baseValue = registerFile.getRegisterValue(src2);
                    int offset = Integer.parseInt(src1);
                    double effectiveAddress = baseValue + offset;
                    entry.setVk(effectiveAddress);
//                    registerFile.setRegisterQi(src2, this.name);
                }
            }

            // Get data to store from the destination register
            if (registerFile.getRegisterQi(dest) != null && !registerFile.getRegisterQi(dest).isEmpty()) {
                ArrayList<ArrayList<String>> destQi = registerFile.getRegisterQi(dest);
                if (destQi != null && !destQi.isEmpty()) {
                    entry.addQj(new ReservationStationEntry.Pair(destQi.getLast().getFirst(), Integer.parseInt(destQi.getLast().getLast())));
                }
            } else {
                entry.setVj(registerFile.getRegisterValue(dest));
            }
        }// Handle Branch instructions
        else if (opcode.equals("BNE") || opcode.equals("BEQ")) {
            // destination is the first register, src1 is the second register, src2 is the branch target (offset)
            // Handle first register dependencies
            if (destination != null && !destination.isEmpty()) {
                ArrayList<ArrayList<String>> destQi = registerFile.getRegisterQi(destination);
                if (destQi != null && !destQi.isEmpty()) {
                    entry.addQj(new ReservationStationEntry.Pair(destQi.get(destQi.size() - 1).getFirst(), Integer.parseInt(destQi.get(destQi.size() - 1).get(1))));
                } else {
                    entry.setVj(registerFile.getRegisterValue(destination));
                }
            }

            // Handle second register dependencies
            if (src1 != null && !src1.isEmpty()) {
                ArrayList<ArrayList<String>> src1Qi = registerFile.getRegisterQi(src1);
                if (src1Qi != null && !src1Qi.isEmpty()) {
                    entry.addQk(new ReservationStationEntry.Pair(src1Qi.get(src1Qi.size() - 1).getFirst(), Integer.parseInt(src1Qi.get(src1Qi.size() - 1).get(1))));
                } else {
                    entry.setVk(registerFile.getRegisterValue(src1));
                }
            }

            // The branch target (src2) is an immediate value (offset), so store it
            entry.setImmediate(Integer.parseInt(src2));
        } else {
            // Check Qi for source1 from registerFile
            if (src1 != null && !src1.isEmpty()) {
                ArrayList<ArrayList<String>> src1Qi = registerFile.getRegisterQi(src1);
                if (src1Qi != null && !src1Qi.isEmpty()) {
                    entry.addQj(new ReservationStationEntry.Pair(src1Qi.getLast().getFirst(), Integer.parseInt(src1Qi.getLast().getLast())));
                } else {
                    entry.setVj(registerFile.getRegisterValue(src1));
                }
            }

            // Check Qi for source2 from registerFile
            if (src2 != null && !src2.isEmpty()) {
                ArrayList<ArrayList<String>> src2Qi = registerFile.getRegisterQi(src2);
                if (src2Qi != null && !src2Qi.isEmpty()) {
                    entry.addQk(new ReservationStationEntry.Pair(src2Qi.getLast().get(0), Integer.parseInt(src2Qi.getLast().getLast())));
                } else {
                    if (src2.charAt(0) != 'F' && src2.charAt(0) != 'R')
                        entry.setVk(Integer.parseInt(src2));
                    else
                        entry.setVk(registerFile.getRegisterValue(src2));
                }
            }
        }
        if (!opcode.equals("BNE") && !opcode.equals("BEQ") && !opcode.equals("S.D") && !opcode.equals("S.S") && !opcode.equals("SD") && !opcode.equals("SW")) {
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(this.name);
            arrayList.add(inst.getInstructionNumber() + "");
            registerFile.setRegisterQi(destination, arrayList);
        }
        entries.add(entry);
        //System.out.println("Issued instruction: " + opcode + " to " + this.name);

    }


    // src/main/java/com/microproject/microproject/model/ReservationStation.java

    public void execute(RegisterFile registerFile) {
        for (ReservationStationEntry entry : entries) {
            entry.execute(registerFile);
        }
    }

    // ReservationStation.java
    public void writeBack(CommonDataBus cdb, RegisterFile registerFile) {
        for (ReservationStationEntry entry : entries) {
            if (entry.isExecutionComplete() && !entry.isResultWritten()) {
                String opcode = entry.getInstruction().getOpcode();
                String destination = entry.getDestination();
                double result = entry.getResult();

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

    public void updateEntries(CDBEntry entry) {
        for (ReservationStationEntry rsEntry : entries) {
            if(rsEntry.getQj() != null && !rsEntry.getQj().isEmpty()) {
                for (int i = 0; i < rsEntry.getQj().size(); i++) {
                    if (entry.getInstructionNumber() == rsEntry.getQj().get(i).instructionNumber) {
                        if (entry.getDestination().equals(rsEntry.getInstruction().getSource1())) {
                            rsEntry.setVj(entry.getResult());
                            rsEntry.setQj(new ArrayList<>());
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

//            if (entry.getDestination().equals(rsEntry.getInstruction().getSource1())) {
//                rsEntry.setVj(entry.getResult());
//                rsEntry.setQj(null);
//            }
//            if ("BNE".equals(rsEntry.getInstruction().getOpcode()) || "BEQ".equals(rsEntry.getInstruction().getOpcode())) {
//                if (entry.getDestination().equals(rsEntry.getInstruction().getDestination())) {
//                    rsEntry.setVj(entry.getResult());
//                    rsEntry.setQj(null);
//                }
//            }
            for(int i = 0; i < rsEntry.getQk().size();i++){
                if(entry.getInstructionNumber() == rsEntry.getQk().get(i).instructionNumber) {
                    if (entry.getDestination().equals(rsEntry.getInstruction().getSource2())) {
                        rsEntry.setVk(entry.getResult());
                        rsEntry.setQk(null);
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

//            if (entry.getDestination().equals(rsEntry.getInstruction().getSource2())) {
//                rsEntry.setVk(entry.getResult());
//                rsEntry.setQk(null);
//            }
//            if ("S.D".equals(rsEntry.getInstruction().getOpcode()) ||
//                    "S.S".equals(rsEntry.getInstruction().getOpcode()) ||
//                    "SD".equals(rsEntry.getInstruction().getOpcode()) ||
//                    "SW".equals(rsEntry.getInstruction().getOpcode())) {
//
//                if (entry.getDestination().equals(rsEntry.getInstruction().getDestination())) {
//                    rsEntry.setVj(entry.getResult());
//                    rsEntry.setQj(null);
//                }
//                if (entry.getSrc2() != null && entry.getSrc2().equals(rsEntry.getInstruction().getSource2())) {
//                    rsEntry.setVk(registerFile.getRegisterValue(entry.getSrc2()));
//                    rsEntry.setQk(null);
//                }
//            }

        }
    }

    public void markResultWritten(String destination) {
        for (ReservationStationEntry entry : entries) {
            if (entry.getDestination() != null && entry.getDestination().equals(destination)) {
                entry.setResultWritten(true);
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