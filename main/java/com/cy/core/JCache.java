package com.cy.core;
import com.cy.api.CacheExpire;
import com.cy.core.expire.CacheExpireSimple;
import com.cy.core.expire.CacheExpireSorted;
import com.cy.core.model.ValueHolder;

import java.util.concurrent.ConcurrentHashMap;
public class JCache<K,V>{
    private final ConcurrentHashMap<K, ValueHolder<V>> cache = new ConcurrentHashMap<>();
//    private final CacheExpire cacheExpire = new CacheExpireSimple(this);
    private final CacheExpire cacheExpire = new CacheExpireSorted(this);
    private int size;
    public JCache(int size) {
        this.size = size;
    }
    public String set(K key, V value, long expireAt) {
        if(key == null) return "null\r\n";
        cache.put(key, new ValueHolder<>(value, expireAt));
        return "OK\r\n";
    }
    public ValueHolder get(K key) {
        this.cacheExpire.refresh(key);
        return cache.get(key);
    }
    public boolean contains(K key) {
        return cache.containsKey(key);
    }
    public String remove(K key) {
        if(cache.remove(key)!=null) {
            return "OK\r\n";
        }
        return "null\r\n";
    }
    public JCache<K,V> expire(K key, long time){
        long expireTime = System.currentTimeMillis() + time;
        ValueHolder<V> valueHolder = cache.get(key);
        if(valueHolder==null){
            return null;
        }
        valueHolder.setExpireAt(expireTime);
        return this.expireAt(key, expireTime);
    }
    public JCache<K,V> expireAt(K key, long expireTime){
        this.cacheExpire.expire(key, expireTime);
        return this;
    }
}
