package com.cy.core.evict;

import com.cy.api.CacheEvict;
import com.cy.core.cacheEntry.EvictCache;
import com.cy.core.model.CircleNode;
import com.cy.core.model.Node;

import java.util.HashMap;
import java.util.Map;

public class EvictClock<K,V> implements CacheEvict<K,V> {

    @Override
    public Node<K, V> put(EvictCache<K, V> cache, K key, V value, long expireAt) {
        CircleNode<K,V> tmp = new CircleNode<>(key,value,expireAt);
        tmp.setAccess(false);
        if(cache.getHead().next == null){
            tmp.prev = tmp;
            tmp.next = tmp;
            cache.getHead().next = tmp;
        }
        else{
            insertIntoTail(cache,tmp);
        }
        return tmp;
    }

    @Override
    public Map<K, Node<K, V>> initMap() {
        return new HashMap<>();
    }

    @Override
    public EvictCache<K, V> doEvict(EvictCache<K, V> cache) {
        Node<K,V> head = cache.getHead();
        while(true){
            CircleNode<K,V> tmp = (CircleNode<K, V>) head.next.next;
            while(tmp!=head.next){
                if(tmp.getAccess()){
                    tmp.setAccess(false);
                }
                else {
                    head.next = tmp.next;
                    cutFrom(cache, tmp.getKey());
                    cache.remove(tmp.getKey());
                    return cache;
                }
                tmp=(CircleNode<K, V>) tmp.next;
                }
            head.next=tmp.prev;
        }
    }

    @Override
    public EvictCache<K, V> insertIntoTail(EvictCache<K, V> cache, Node<K, V> newValue) {
        Node<K,V> head = cache.getHead().next;
        newValue.next = head;
        newValue.prev = head.prev;
        head.prev.next = newValue;
        head.prev = newValue;
        return cache;
    }

    @Override
    public Node<K, V> cutFrom(EvictCache<K, V> cache, K key) {
        CircleNode<K,V> tmp = (CircleNode<K, V>) cache.getNode(key);
        if(tmp==null){
            return null;
        }
        else if(cache.getSize()<=1){
            cache.getHead().next = null;
        }
        else if(cache.getHead().next == tmp){
            cache.getHead().next = tmp.next;}
        else{
            tmp.prev.next = tmp.next;
            tmp.next.prev = tmp.prev;
        }
        return tmp;
    }

    @Override
    public Node<K,V> update(EvictCache<K, V> cache, final K key) {
        CircleNode<K,V> tmp = (CircleNode<K, V>) cache.getNode(key);
        tmp.setAccess(true);
        return tmp;
    }
}
