package com.yr.sql.common;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @moduleName: ManualCache
 * @description: 本地缓存
 * @author: 杨睿
 * @date: 2021-03-21 10:21
 **/

@Service
public class ManualCache {

    private Cache<String, Object> cache = null;

    public ManualCache() {
        this.cache = Caffeine.newBuilder().expireAfterWrite(12, TimeUnit.HOURS).maximumSize(10_000).build();
    }

    public void put(String key, Object value) {
        this.cache.put(key, value);
    }

    public void removeAll() {
        this.cache.invalidateAll();
    }

    public void remove(String key) {
        this.cache.invalidate(key);
    }

    public Object get(String key) {
        return this.cache.getIfPresent(key);
    }
}
