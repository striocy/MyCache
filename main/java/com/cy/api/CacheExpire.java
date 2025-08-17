package com.cy.api;

public interface CacheExpire<K,V>{
    //Add Key into to-expire list waiting for expire
    void expire(final K key, final long expireAt);
    // lazy start to delete expired key in expireMap
    void refresh(final K key);
    void setCache(Cache<K,V> cache);
}

