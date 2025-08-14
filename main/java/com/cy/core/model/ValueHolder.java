package com.cy.core.model;

public class ValueHolder<V> {
    private V value;
    private long expireAt;
    public ValueHolder(V value, long expireAt) {
        this.value = value;
        this.expireAt = expireAt;
    }
    public V getValue() {
        return value;
    }
    public long expireAt() {
        return expireAt;
    }
    public void setValue(V value) {
        this.value = value;
    }
    public void setExpireAt(long expireAt) {
        this.expireAt = expireAt;
    }
}
