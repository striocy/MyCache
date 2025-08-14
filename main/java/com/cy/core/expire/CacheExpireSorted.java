package com.cy.core.expire;

import com.cy.api.CacheExpire;
import com.cy.core.JCache;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import static java.lang.Thread.sleep;

//use expireTime as key
//LinkedList used to record and enable the access of hashMap in a order
public class CacheExpireSorted<K,V> implements CacheExpire<K,V> {
    private final ConcurrentHashMap<Long, LinkedList<K>> expireMap = new ConcurrentHashMap<>();
    private final Set<Long> keys = new ConcurrentSkipListSet<>();
    private final JCache<K,V> cache;

    public CacheExpireSorted(JCache<K,V> cache) {
        this.cache = cache;
        scheduledTask st=new scheduledTask();
        st.run();
    }
    @Override
    public void expire (final K key, final long expireAt) {
        LinkedList<K> list = expireMap.get(expireAt);
        if( list == null) {
            list = new LinkedList<>();
            keys.add(expireAt);
        }
        list.add(key);
        expireMap.put(expireAt, list);
    }
    @Override
    public void refresh(K key) {
        long expireAt = this.cache.get(key).expireAt();
        if(expireAt > System.currentTimeMillis()){
            return;
        }
        LinkedList<K> list = expireMap.get(expireAt);
        remove(expireAt);
    }
    public void remove(long expireAt) {
        LinkedList<K> list = expireMap.get(expireAt);
        Iterator<K> it = list.iterator();
        while(it.hasNext()) {
            K key = it.next();
            cache.remove(key);
            it.remove();
        }
        expireMap.remove(expireAt);
        keys.remove(expireAt);
    }
    private class scheduledTask implements Runnable{
        @Override
        public void run() {
            while(true){
                if(!expireMap.isEmpty()){
                    continue;
                }
                long currentTime = System.currentTimeMillis();
                Iterator<Long> it = keys.iterator();
                while(it.hasNext()) {
                    long expireAt = it.next();
                    if(expireAt > currentTime) {
                        break;
                    }
                remove(expireAt);
            }
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
        }
    }}
}
