package com.cy.core.evict;

import com.cy.api.CacheEvict;
import com.cy.core.cacheEntry.EvictCache;
import com.cy.core.cacheEntry.JCache;
import com.cy.core.model.Node;

public class EvictFIFO<K,V> implements CacheEvict<K,V> {
    @Override
    public EvictCache<K,V> doEvict(EvictCache<K,V> cache) {
        Node<K,V> tmp = cutFrom(cache, cache.getHead().getKey());
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
            cache.setHead(null);
            cache.setTail(null);
        }
        else if(tmp.prev==null){
            cache.setHead(tmp.next);
        }
        else if(tmp.next==null){
            tmp.prev.next = tmp.next;
        }
        else{
            Node<K,V> prev = tmp.prev;
            prev.next = tmp.next;
            tmp.next.prev = prev;
        }
        return tmp;
    }
}
