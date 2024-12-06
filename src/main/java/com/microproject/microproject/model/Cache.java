// Java
package com.microproject.microproject.model;

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


        // Cache hit
        return cacheBlock.getData();

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