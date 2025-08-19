package com.cy.core.model;

public class Node<K,V> extends RDBData<K,V> {
    public Node<K,V> next;
    public Node<K,V> prev;
    public Node(){}
    public Node(K key, V value, long expireAt) {
        super(key, value, expireAt);
    }
}
