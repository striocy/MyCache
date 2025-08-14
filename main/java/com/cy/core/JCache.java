package com.cy.core;
import com.cy.api.CacheExpire;
import com.cy.core.expire.CacheExpireSimple;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
public class JCache<K,V>{
    private final ConcurrentHashMap<K, V> cache = new ConcurrentHashMap<>();
    private final CacheExpire cacheExpire = new CacheExpireSimple(this);
    private final int size;
    public JCache(int size) {
        this.size = size;
    }
    public String set(K key, V value) {
        if(key == null) return "null\r\n";
        cache.put(key, value);
        return "OK\r\n";
    }
    public V get(K key) {
        this.cacheExpire.refresh(key);
        return cache.get(key);
    }
    public boolean contains(K key) {
        return cache.containsKey(key);
    }
    public String remove(K key) {
        if(cache.remove(key)!=null) return "OK\r\n";
        return "null\r\n";
    }
    public JCache<K,V> expire(K key, long time){
        long expireTime = System.currentTimeMillis() + time;
        return this.expireAt(key, expireTime);
    }
    public JCache<K,V> expireAt(K key, long expireTime){
        this.cacheExpire.expire(key, expireTime);
        return this;
    }
}
