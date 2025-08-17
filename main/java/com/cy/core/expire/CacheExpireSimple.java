package com.cy.core.expire;
import com.cy.api.Cache;
import com.cy.api.CacheExpire;
import com.cy.core.cacheEntry.JCache;


import java.util.HashMap;
import java.util.Map;

public class CacheExpireSimple<K,V> implements CacheExpire<K,V>{
    private final Map<K, Long> expireMap = new HashMap<>();
    private Cache<K,V> cache;
    public CacheExpireSimple() {
        ExpireThread expireThread = new ExpireThread();
        expireThread.start();
    }
    @Override
    public void setCache(Cache<K, V> cache) {
        this.cache = cache;
    }

    @Override
    public void expire(K key, long expireAt) {
        expireMap.put(key,expireAt);
    }

    @Override
    public void refresh(K key) {
            Long expireAt = expireMap.get(key);
            if(System.currentTimeMillis()>expireAt){
                cache.remove(key);
        }
    }
    private class ExpireThread extends  Thread{
        @Override
        public void run() {
            while(true){
                if(!expireMap.isEmpty()){
                for(Map.Entry<K, Long> entry :expireMap.entrySet()){
                    Long expireAt = entry.getValue();
                    if(expireAt < System.currentTimeMillis()){
                        expireMap.remove(entry.getKey());
                        cache.remove(entry.getKey());
                    }
                }
                }
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
