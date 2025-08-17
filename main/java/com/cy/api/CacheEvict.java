package com.cy.api;

import com.cy.core.cacheEntry.EvictCache;
import com.cy.core.model.Node;

public interface CacheEvict<K,V> {
     EvictCache<K,V> doEvict(final EvictCache<K,V> cache);
     EvictCache<K,V> insertIntoTail(EvictCache<K,V> cache, final Node<K,V> newValue);
     Node<K,V> cutFrom(EvictCache<K,V> cache, K key);
}
