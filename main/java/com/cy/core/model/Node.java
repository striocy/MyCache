package com.cy.core.model;

public class Node<K,V>{
    private K key;
    private V value;
    private long expireAt;
    public Node<K,V> next;
    public Node<K,V> prev;
    public Node(K key, V value, long expireAt) {
        this.key = key;
        this.value = value;
        this.expireAt = expireAt;
    }
    public K getKey(){
        return key;
    }
    public V getValue(){
        return value;
    }
    public void setValue(V value){
        this.value = value;
    }
    public long getExpireAt(){
        return expireAt;
    }
    public void setExpireAt(long expireAt){
        this.expireAt = expireAt;
    }
}
