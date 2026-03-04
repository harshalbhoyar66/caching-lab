package com.cachinglab.caching_lab.cache;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class AdvancedCache<K, V> {

    private final int maxSize;
    private final long ttlMillis;

    private final AtomicLong hitCount = new AtomicLong();
    private final AtomicLong missCount = new AtomicLong();

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

    private final Map<K, CacheObject<V>> cache;

    public AdvancedCache(int maxSize, long ttlMillis) {
        this.maxSize = maxSize;
        this.ttlMillis = ttlMillis;

        this.cache = Collections.synchronizedMap(
                new LinkedHashMap<K, CacheObject<V>>(16, 0.75f, true) {
                    protected boolean removeEldestEntry(Map.Entry<K, CacheObject<V>> eldest) {
                        return size() > AdvancedCache.this.maxSize;
                    }
                }
        );
    }

    public V get(K key) {
        CacheObject<V> obj = cache.get(key);

        if (obj == null) {
            missCount.incrementAndGet();
            return null;
        }

        if (obj.isExpired()) {
            cache.remove(key);
            missCount.incrementAndGet();
            return null;
        }

        hitCount.incrementAndGet();
        return obj.value;
    }

    public void put(K key, V value) {
        cache.put(key, new CacheObject<>(value, ttlMillis));
    }

    public void evict(K key) {
        cache.remove(key);
    }

    public void clear() {
        cache.clear();
    }

    public int size() {
        return cache.size();
    }

    public long getHitCount() {
        return hitCount.get();
    }

    public long getMissCount() {
        return missCount.get();
    }

    public double getHitRatio() {
        long hits = hitCount.get();
        long misses = missCount.get();
        return (hits + misses) == 0 ? 0 : (double) hits / (hits + misses);
    }
}