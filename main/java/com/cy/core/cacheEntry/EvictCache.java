package com.cy.core.cacheEntry;

import com.cy.api.Cache;
import com.cy.api.CacheEvict;
import com.cy.api.CacheExpire;
import com.cy.core.model.Node;
import com.cy.core.model.ValueHolder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EvictCache<K,V> implements Cache<K,V> {
    private final Map<K, Node<K,V>> cache = new ConcurrentHashMap<>();
    private final int size;
    private Node<K,V> head;
    private Node<K,V> tail;
    private final CacheEvict<K,V> evictor;
    private  CacheExpire<K,V> expirer;
    public EvictCache(int MAX_size, CacheEvict<K,V> evictor, CacheExpire<K,V> expire) {
        this.evictor = evictor;
        this.expirer = expire;
        this.size =  MAX_size;
        head = tail = null;
    }
    @Override
    public String set(K key, V value, long expireAt) {
        Node<K,V> tmp = new Node<>(key,value,expireAt);
        if(head == null){
            head = tmp;
            tail = tmp;
        }
        if(cache.size()>=size){
            evictor.doEvict(this);
        }
        evictor.insertIntoTail(this, tmp);
        cache.put(key, tmp);
        expire(key, expireAt);
        return "ok\n";
    }
    @Override
    public V get(K key) {
        if(cache.get(key) == null){
            return null;
        }
        if(cache.size()<=1){
            return cache.get(key).getValue();
        }
        Node<K,V> tmp = evictor.cutFrom(this, key);
        evictor.insertIntoTail(this, tmp);
        return tmp.getValue();
    }

    @Override
    public boolean contains(K key) {
        return cache.containsKey(key);
    }

    @Override
    public String remove(K key) {
        evictor.cutFrom(this,key);
        cache.remove(key);
        return "ok\n";
    }

    @Override
    public Cache<K, V> expire(K key, long expireAt) {
        if(expireAt<=0){
            return this;
        }
        else {
            long expireTime = System.currentTimeMillis()+expireAt;
            cache.get(key).setExpireAt(expireTime);
            expirer.expire(key, expireTime);
        }
        return this;
    }
    @Override
    public long getExpireAt(K key) {
        return cache.get(key).getExpireAt();
    }
    public Node<K,V> getHead() {
        return head;
    }
    public Node<K,V> getTail() {
        return tail;
    }
    public void setHead(Node<K,V> head) {
        this.head = head;
    }
    public Node<K,V> getNode(K key) {
        return cache.get(key);
    }
    public void setTail(Node<K,V> tail) {
        this.tail = tail;
    }

    public int getSize() {
        return cache.size();
    }
    @Override
    public CacheExpire<K, V> getExpirer() {
        return expirer;
    }
}
