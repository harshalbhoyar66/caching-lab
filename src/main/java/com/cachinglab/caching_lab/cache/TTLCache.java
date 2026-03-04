package com.cachinglab.caching_lab.cache;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

public class TTLCache<K, V> {

    private static class CacheObject<V> {
        V value;
        long expiryTime;

        CacheObject(V value, long ttlMillis) {
            this.value = value;
            this.expiryTime = Instant.now().toEpochMilli() + ttlMillis;
        }

        boolean isExpired() {
            return Instant.now().toEpochMilli() > expiryTime;
        }
    }

    private final ConcurrentHashMap<K, CacheObject<V>> cache = new ConcurrentHashMap<>();

    public void put(K key, V value, long ttlMillis) {
        cache.put(key, new CacheObject<>(value, ttlMillis));
    }

    public V get(K key) {
        CacheObject<V> obj = cache.get(key);

        if (obj == null) return null;

        if (obj.isExpired()) {
            cache.remove(key);
            return null;
        }

        return obj.value;
    }

    public void evict(K key) {
        cache.remove(key);
    }
}