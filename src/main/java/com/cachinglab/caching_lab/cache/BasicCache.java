package com.cachinglab.caching_lab.cache;

import java.util.concurrent.ConcurrentHashMap;

public class BasicCache<K, V> {

    private final ConcurrentHashMap<K, V> cache = new ConcurrentHashMap<>();

    public void put(K key, V value) {
        cache.put(key, value);
    }

    public V get(K key) {
        return cache.get(key);
    }

    public void evict(K key) {
        cache.remove(key);
    }
}