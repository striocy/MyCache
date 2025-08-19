package com.cy.core.cacheEntry;

import com.cy.api.Cache;
import com.cy.api.CacheEvict;
import com.cy.api.CacheExpire;
import com.cy.api.PersistenceManager;
import com.cy.core.exception.PersistenceException;
import com.cy.core.model.Node;
import com.cy.core.model.RDBData;
import com.cy.core.persist.RDB;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EvictCache<K,V> implements Cache<K,V> {
    private final Map<K, Node<K,V>> cache;
    private final int size;
    private Node<K,V> head;
    private Node<K,V> tail;
    private final CacheEvict<K,V> evictor;
    private  CacheExpire<K,V> expirer;
    private PersistenceManager persist;
    public EvictCache(int MAX_size, CacheEvict<K,V> evictor, CacheExpire<K,V> expire, File file) {
        this.evictor = evictor;
        this.cache = evictor.initMap();
        this.expirer = expire;
        this.size =  MAX_size;
        head = tail = new Node<>();
        this.persist = new RDB<>(file);
    }
    @Override
    public String set(K key, V value, long expireAt) {
        if(cache.size()>=size){
            evictor.doEvict(this);
        }
        Node<K,V> tmp = evictor.put(this, key, value, expireAt);
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
        return evictor.update(this, key).getValue();
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

    @Override
    public Iterator<? extends RDBData<K,V>> valueIterator() {
        return cache.values().iterator();
    }

    @Override
    public String save() throws FileNotFoundException, PersistenceException {
        persist.save(this);
        return "OK\n";
    }

    @Override
    public String load() throws PersistenceException, FileNotFoundException {
        persist.load(this);
        return "OK\n";
    }
}
