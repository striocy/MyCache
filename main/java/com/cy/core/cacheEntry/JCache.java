package com.cy.core.cacheEntry;
import com.cy.api.Cache;
import com.cy.api.CacheExpire;
import com.cy.core.expire.CacheExpireSorted;
import com.cy.core.model.ValueHolder;

import java.util.concurrent.ConcurrentHashMap;
public class JCache<K,V> implements Cache<K,V> {
    private final ConcurrentHashMap<K, ValueHolder<V>> cache = new ConcurrentHashMap<>();
//    private final CacheExpire cacheExpire = new CacheExpireSimple(this);
    private final CacheExpire<K,V> cacheExpire = new CacheExpireSorted<>();
    private int size;
    public JCache(int size) {
        this.size = size;
    }
    @Override
    public String set(K key, V value, long expireAt) {
        if(key == null) return "null\r\n";
        cache.put(key, new ValueHolder<>(value,expireAt));
        return "OK\r\n";
    }
    @Override
    public V get(K key) {
        this.cacheExpire.refresh(key);
        return cache.get(key).getValue();
    }
    @Override
    public boolean contains(K key) {
        return cache.containsKey(key);
    }
    @Override
    public String remove(K key) {
        if(cache.remove(key)!=null) {
            return "OK\r\n";
        }
        return "null\r\n";
    }
    @Override
    public JCache<K,V> expire(K key, long time){
        long expireTime = System.currentTimeMillis() + time;
        ValueHolder<V> valueHolder = cache.get(key);
        if(valueHolder==null){
            return null;
        }
        valueHolder.setExpireAt(expireTime);
        return this.expireAt(key, expireTime);
    }
    @Override
    public long getExpireAt(K key){
        return cache.get(key).expireAt();
    }

    @Override
    public CacheExpire<K, V> getExpirer() {
        return this.cacheExpire;
    }

    public JCache<K,V> expireAt(K key, long expireTime){
        this.cacheExpire.expire(key, expireTime);
        return this;
    }
}
