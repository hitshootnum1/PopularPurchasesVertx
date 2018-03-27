package com.example.warehouse.handler.cache;

import com.example.warehouse.handler.IdentifyAble;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheManagement {

    private final Map<String, Object> CACHE = new ConcurrentHashMap<>();

    private static final CacheManagement CACHE_MANAGEMENT = new CacheManagement();

    public static CacheManagement getInstance() {
        return CACHE_MANAGEMENT;
    }

    public void put(String key, Object value) {
        CACHE.put(key, value);
    }

    public void putIfNotExists(String key, Object value) {
        if (!CACHE.containsKey(key)) {
            CACHE.put(key, value);
        }
    }

    public void putIfNotExists(IdentifyAble identifyAble) {
        String identity = identifyAble.getIdentity();
        if (identity != null) {
            if (!CACHE.containsKey(identity)) {
                CACHE.put(identifyAble.getIdentity(), identifyAble);
            }
        }
    }

    public <T> T get(String key, Class<T> clazz) {
        Object value = CACHE.get(key);
        if (value != null) {
            try {
                return clazz.cast(value);
            } catch (ClassCastException ex) {
                return null;
            }
        } else {
            return null;
        }
    }

    public void invalidCache() {
        CACHE.clear();
    }
}
