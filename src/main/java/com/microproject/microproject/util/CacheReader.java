package com.microproject.microproject.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CacheReader {

    /**
     * Reads cache configuration from the specified file.
     *
     * @param filePath Path to the cache configuration file.
     * @return List of integers representing cacheSize, blockSize, associativity, latency.
     * @throws IOException If an I/O error occurs reading from the file.
     */
    public static List<Integer> readCacheConfig(String filePath) throws IOException {
        List<Integer> config = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            if (line != null) {
                String[] parts = line.split(",");
                if (parts.length != 4) {
                    throw new IllegalArgumentException("Invalid cache configuration format. Expected 4 integers separated by commas.");
                }
                for (String part : parts) {
                    config.add(Integer.parseInt(part.trim()));
                }
            } else {
                throw new IllegalArgumentException("Cache configuration file is empty.");
            }
        }

        return config;
    }
}