package com.cy.api;

import com.cy.core.exception.PersistenceException;
import com.cy.core.model.Node;
import com.cy.core.model.RDBData;
import com.cy.core.model.ValueHolder;

import java.io.FileNotFoundException;
import java.util.Iterator;

public interface Cache<K,V> {
    String set(K key,V value, long expireAt);
    V get(K key);
    boolean contains(K key);
    String remove(K key);
    Cache<K,V> expire(K key, long expireAt);
    long getExpireAt(K key);
    CacheExpire<K,V> getExpirer();
    Iterator<? extends RDBData<K,V>> valueIterator();
    String save() throws FileNotFoundException, PersistenceException;
    String load() throws FileNotFoundException, PersistenceException;
}
