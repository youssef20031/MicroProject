// Java
package com.microproject.microproject.model;

public class Cache {
    private static int blockSize;
    private static int cacheSize;
    private static int cacheHitLatency;
    private static int cacheMissPenalty;
    private static CacheBlock[] cache;

    // Constructor to configure the cache
    public Cache(int blockSize, int cacheSize, int cacheHitLatency, int cacheMissPenalty) {
        Cache.blockSize = blockSize;
        Cache.cacheSize = cacheSize;
        Cache.cacheHitLatency = cacheHitLatency;
        Cache.cacheMissPenalty = cacheMissPenalty;
        cache = new CacheBlock[cacheSize];
    }

    // Method to access data in the cache
    public static int accessData(int address) {
        int blockAddress = address / blockSize;
        int cacheIndex = blockAddress % cacheSize;
        CacheBlock cacheBlock = cache[cacheIndex];

        if (cacheBlock != null && cacheBlock.isValid() && cacheBlock.getAddress() == blockAddress) {
            // Cache hit
            return cacheHitLatency;
        } else {
            // Cache miss
            loadBlockToCache(blockAddress, cacheIndex);
            return cacheMissPenalty;
        }
    }

    // Method to load a block into the cache
    private static void loadBlockToCache(int blockAddress, int cacheIndex) {
        cache[cacheIndex] = new CacheBlock(blockAddress, blockSize); // Initialize block with blockSize
    }

    //add data to cache
    public static void addData(int address, Register[] data) {
        int blockAddress = address / blockSize;
        int cacheIndex = blockAddress % cacheSize;
        CacheBlock cacheBlock = cache[cacheIndex];
        cacheBlock.setData(data);
    }
}