package com.cy.core.evict;

import com.cy.api.Cache;
import com.cy.api.CacheEvict;
import com.cy.core.cacheEntry.EvictCache;
import com.cy.core.cacheEntry.JCache;
import com.cy.core.model.Node;

import java.util.HashMap;
import java.util.Map;

public class EvictFIFO<K,V> implements CacheEvict<K,V> {
    @Override
    public Node<K, V> put(EvictCache<K,V> cache, K key, V value, long expireAt) {
        Node<K,V> tmp = new Node<>(key,value,expireAt);
        if(cache.getHead().next == null){
            tmp.prev = cache.getHead();
            cache.getHead().next = tmp;
            cache.setTail(tmp);
        }
        else{
            insertIntoTail(cache,tmp);
        }
        return tmp;
    }

    @Override
    public Map<K, Node<K,V>> initMap() {
        return new HashMap<>();
    }

    @Override
    public EvictCache<K,V> doEvict(EvictCache<K,V> cache) {
        Node<K,V> tmp = cutFrom(cache, cache.getHead().next.getKey());
        cache.remove(tmp.getKey());
        return cache;
    }
    @Override
    public EvictCache<K,V> insertIntoTail(EvictCache<K,V> cache, final Node<K,V> newValue) {
        cache.getTail().next = newValue;
        newValue.prev = cache.getTail();
        cache.setTail(newValue);
        return cache;
    }
    @Override
    public Node<K,V> cutFrom(EvictCache<K,V> cache, final K key) {
        Node<K,V> tmp = cache.getNode(key);
        if(tmp==null){
            return null;
        }
        if(cache.getSize()<=1){
            cache.getHead().next = null;
            cache.setTail(null);
        }
        else if(tmp.prev==cache.getHead()){
            tmp.prev.next = tmp.next;
            tmp.next.prev = tmp.prev;
        }
        else if(tmp.next==null){
            tmp.prev.next = null;
            cache.setTail(tmp.prev);
        }
        else{
            tmp.prev.next = tmp.next;
            tmp.next.prev = tmp.prev;
        }
        return tmp;
    }

    @Override
    public Node<K,V> update(EvictCache<K, V> cache, K key) {
        return cache.getNode(key);
    }
}
