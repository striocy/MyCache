package com.cy.core.evict;

import com.cy.api.CacheEvict;
import com.cy.core.cacheEntry.EvictCache;
import com.cy.core.model.Node;

import java.util.HashMap;
import java.util.Map;

public class EvictLRU<K,V> extends EvictFIFO<K,V> {
    @Override
    public Node<K, V> update(EvictCache<K, V> cache, K key) {
        Node<K,V> tmp = cutFrom(cache, key);
        insertIntoTail(cache, tmp);
        return tmp;
    }
}
