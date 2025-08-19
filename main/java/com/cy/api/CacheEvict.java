package com.cy.api;

import com.cy.core.cacheEntry.EvictCache;
import com.cy.core.model.Node;

import java.util.Map;

public interface CacheEvict<K,V> {
     Node<K,V> put(final EvictCache<K,V> cache, K key, V value, long expireAt);
     Map<K,Node<K,V>> initMap();
     EvictCache<K,V> doEvict(final EvictCache<K,V> cache);
     EvictCache<K,V> insertIntoTail(EvictCache<K,V> cache, final Node<K,V> newValue);
     Node<K,V> cutFrom(EvictCache<K,V> cache, K key);
     Node<K,V> update(final EvictCache<K,V> cache, final K key);
}
