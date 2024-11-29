// // Java
// package com.microproject.microproject.model;

// public class InstructionSet {

//     public static void readInstruction(Instruction instruction, RegisterFile registerFile){
//         String opcode = instruction.getOpcode();
//         String destination = instruction.getDestination();
//         String source1 = instruction.getSource1();
//         String source2 = instruction.getSource2();
//         int index1 = source1 != null ? Integer.parseInt(source1.substring(1)) : -1;
//         int index2 = source2 != null ? Integer.parseInt(source2.substring(1)) : -1;
//         int destIndex = destination != null ? Integer.parseInt(destination.substring(1)) : -1;

//         Register register1 = index1 != -1 ? registerFile.getRegister(index1) : null;
//         Register register2 = index2 != -1 ? registerFile.getRegister(index2) : null;
//         double value1 = register1 != null ? register1.getValue() : 0;
//         double value2 = register2 != null ? register2.getValue() : 0;
//         double result = 0;

        

//         switch (opcode) {
//             case "ADD.D":
//             case "ADD.S":
//                 result = value1 + value2;
//                 break;
//             case "SUB.D":
//             case "SUB.S":
//                 result = value1 - value2;
//                 break;
//             case "MUL.D":
//             case "MUL.S":
//                 result = value1 * value2;
//                 break;
//             case "DIV.D":
//             case "DIV.S":
//                 result = value1 / value2;
//                 break;
//             case "L.S":
//             case "L.D":
//                 result = value1; // Assuming load operation
//                 break;
//             case "S.S":
//             case "S.D":
//                 result = value1; // Assuming store operation
//                 break;
//             default:
//                 System.out.println("Unknown opcode: " + opcode);
//                 return;
//         }


//         if (destIndex != -1) {
//             registerFile.setRegister(destIndex, new Register(destination, result, ""));
//         }
//     }

//     public static void main(String[] args){
//         Instruction instruction = new Instruction("SUB.S", 5, "F1", "F2", "F3");
//         RegisterFile registerFile = new RegisterFile();
//         registerFile.setRegister(3, new Register("F3", 1, ""));
//         registerFile.setRegister(2, new Register("F2", 2, ""));
//         System.out.println(registerFile);

//         readInstruction(instruction, registerFile);

//         System.out.println(registerFile);
//     }
// }