package com.cy.core.model;

import com.cy.api.Entry;


public class CacheEntry<K,V> implements Entry<K,V> {
    private final K key;
    private final V value;
    private final Long expireAt;
    public CacheEntry(K key, V value, Long expireAt) {
        this.key = key;
        this.value = value;
        this.expireAt = expireAt;
    }
    @Override
    public K key() {
        return key;
    }
    @Override
    public V value() {
        return value;
    }
    public Long getExpireAt() {
        return expireAt;
    }
}
