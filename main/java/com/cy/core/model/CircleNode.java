package com.cy.core.model;

public class CircleNode<K,V> extends Node<K,V> {
    private Boolean access;
    public CircleNode(final K key, final V value, final long expireAt) {
        super(key, value, expireAt);
    }

    public Boolean getAccess() {
        return access;
    }
    public void setAccess(Boolean access) {
        this.access = access;
    }
}
