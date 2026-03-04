package com.cachinglab.caching_lab.cache;

import java.util.*;

public class LRUCache<K, V> {

    private final int maxSize;

    private final Map<K, V> cache;

    public LRUCache(int maxSize) {
        this.maxSize = maxSize;

        this.cache = Collections.synchronizedMap(
                new LinkedHashMap<K, V>(16, 0.75f, true) {
                    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                        return size() > LRUCache.this.maxSize;
                    }
                }
        );
    }

    public V get(K key) {
        return cache.get(key);
    }

    public void put(K key, V value) {
        cache.put(key, value);
    }
}