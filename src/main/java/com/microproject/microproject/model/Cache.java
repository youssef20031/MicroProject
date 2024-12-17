// Java
package com.microproject.microproject.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Cache {
    private static int blockSize;
    private static int cacheSize;
    private static int cacheHitLatency;
    private static int cacheMissPenalty;
    private static CacheBlock[] cache;
    private static boolean gotAccessed = false;

    public Cache(int blockSize, int cacheSize, int cacheHitLatency, int cacheMissPenalty) {
        Cache.blockSize = blockSize;
        Cache.cacheSize = cacheSize;
        Cache.cacheHitLatency = cacheHitLatency;
        Cache.cacheMissPenalty = cacheMissPenalty;
        cache = new CacheBlock[cacheSize];
    }

    // Method to access data in the cache
    public static boolean accessData(int address) {
        int blockAddress = address / blockSize;
        int cacheIndex = blockAddress % cacheSize;
        CacheBlock cacheBlock = cache[cacheIndex];

        if (gotAccessed) {
            return false;
        } else {
            gotAccessed = true;
            return true;
        }
    }

    // Java
    public static void loadBlockWithData(int address, double data) {
        // Calculate number of blocks in cache
        int blockCount = cacheSize / blockSize;

        // Calculate block address and cache index
        int blockAddress = address / blockSize;
        int cacheIndex = blockAddress % blockCount;

        // Create and store the cache block
        CacheBlock cacheBlock = new CacheBlock(blockAddress);
        cacheBlock.setData(data);
        cache[cacheIndex] = cacheBlock;
    }

    //get cacheblock with address from cache
    public static boolean getCacheBlock(int address) {
        for (int i = 0; i < cacheSize; i++) {
            CacheBlock cacheBlock = cache[i];
            if (cacheBlock != null && cacheBlock.getAddress() == address) {
                return false;
            }
        }return true;
    }
    public static double readData(int address) {
        int blockAddress = address / blockSize;
        int cacheIndex = blockAddress % cacheSize;
        CacheBlock cacheBlock = cache[cacheIndex];

        if (cacheBlock == null || cacheBlock.getAddress() != blockAddress) {
            // Cache miss
            System.out.println("Cache miss at address: " + address);
            // Load data from memory
            double data = loadDataFromMemory(address);
            // Create a new CacheBlock and put it in the cache
            cacheBlock = new CacheBlock(blockAddress, data);
            cache[cacheIndex] = cacheBlock;
        } else {
            // Cache hit
            System.out.println("Cache hit at address: " + address);
        }
        return cacheBlock.getData();
    }

    private static double loadDataFromMemory(int address) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("src/main/java/com/microproject/microproject/text/address.txt"));
            for (String line : lines) {
                String[] parts = line.split(",");
                int fileAddress = Integer.parseInt(parts[0].trim());
                double value = Double.parseDouble(parts[1].trim());
                if (fileAddress == address) {
                    return value;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("Address not found in memory: " + address);
    }

    // Write data to cache
    public static void writeData(int address, double data) {
        int blockAddress = address / blockSize;
        int cacheIndex = blockAddress % cacheSize;
        CacheBlock cacheBlock = cache[cacheIndex];

        if (cacheBlock != null && cacheBlock.isValid() && cacheBlock.getAddress() == blockAddress) {
            // Cache hit - write data
            cacheBlock.setData(data);
            cacheBlock.setDirty(true);
        } else {
            // Cache miss - load block into cache and then write data
            loadBlockWithData(address, data);
            //cacheBlock = cache[cacheIndex];
            //cacheBlock.setData(data);
            //cacheBlock.setDirty(true);
        }
    }

    public static boolean isGotAccessed() {
        return gotAccessed;
    }

    public static void setGotAccessed(boolean gotAccessed) {
        Cache.gotAccessed = gotAccessed;
    }

    public static void gotAccessed() {
        gotAccessed = true;
    }

    public static int getCacheMissPenalty() {
        return cacheMissPenalty;
    }

    public static boolean isSlotEmpty(int cacheSlotAddress) {
        /*int blockAddress = cacheSlotAddress / blockSize;
        int cacheIndex = blockAddress % cacheSize;
        CacheBlock cacheBlock = cache[cacheIndex];*/
        return getCacheBlock(cacheSlotAddress);
    }

    public static void addEmptyBlock(int address) {
        int blockAddress = address / blockSize;
        int cacheIndex = blockAddress % cacheSize;
        CacheBlock cacheBlock = new CacheBlock(blockAddress);
        cache[cacheIndex] = cacheBlock;
    }

    public List<CacheEntry> getEntries() {
        List<CacheEntry> entries = new ArrayList<>();
        for (CacheBlock block : cache) {
            if (block!= null && block.isValid()) {
                entries.add(new CacheEntry(block.getAddress(), block.getData()));
            }
        }
        return entries;
    }


    public String toString() {
        for (int i = 0; i < cacheSize; i++) {
            CacheBlock cacheBlock = cache[i];
            if (cacheBlock != null) {
                System.out.println("Block " + i + ": " + cacheBlock.getData());
            }
        }
        return null;
    }
}