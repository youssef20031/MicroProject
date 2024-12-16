package com.microproject.microproject.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadAddressFromText {

    public static ArrayList<int[]> readAddresses(String filePath) {
        ArrayList<int[]> addresses = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    int address = Integer.parseInt(parts[0].trim());
                    int value = Integer.parseInt(parts[1].trim());
                    addresses.add(new int[]{address, value});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addresses;
    }

    public static void main(String[] args) {
        String filePath = "src/main/java/com/microproject/microproject/text/address.txt";
        ArrayList<int[]> addresses = readAddresses(filePath);
        for (int[] address : addresses) {
            System.out.println("Address: " + address[0] + ", Value: " + address[1]);
        }
    }
}