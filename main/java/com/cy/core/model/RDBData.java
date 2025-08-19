package com.cy.core.model;

import java.io.Serializable;

public class RDBData<K,V> implements Serializable {
    private  K key;
    private  V value;
    private long expireAt;
    public RDBData(final K key, final V value, long expireAt) {
        this.key = key;
        this.value = value;
        this.expireAt = expireAt;
    }

    public RDBData() {
    }

    public K getKey() {
        return key;
    }
    public V getValue() {
        return value;
    }
    public void setValue(final V value) {
        this.value = value;
    }
    public long getExpireAt() {
        return expireAt;
    }
    public void setExpireAt(long expireAt) {
        this.expireAt = expireAt;
    }
}
