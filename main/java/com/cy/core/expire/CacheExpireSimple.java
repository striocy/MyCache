package com.cy.core.expire;
import com.cy.api.CacheExpire;
import com.cy.core.JCache;


import java.util.HashMap;
import java.util.Map;

public class CacheExpireSimple<K,V> implements CacheExpire<K,V>{
    private final Map<K, Long> expireMap = new HashMap<>();
    private final JCache<K,V> cache;
    public CacheExpireSimple(JCache cache) {
        this.cache = cache;
        ExpireThread expireThread = new ExpireThread();
        expireThread.start();
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
