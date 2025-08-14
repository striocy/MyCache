package com.cy.api;

import java.util.Collection;

public interface CacheExpire<K,V>{
    void expire(final K key, final long expireAt);
    void refresh(final K key);
}

