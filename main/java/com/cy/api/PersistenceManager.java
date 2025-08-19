package com.cy.api;

import com.cy.core.exception.PersistenceException;
import com.cy.core.model.RDBData;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface PersistenceManager<K,V> {
    /**
     * 保存缓存快照到磁盘
     */
    void save(Cache<K, V> cache) throws PersistenceException, FileNotFoundException;

    /**
     * 从磁盘加载缓存数据
     */
    Cache<K, V> load(Cache<K,V> cache) throws PersistenceException, FileNotFoundException;

    /**
     * 异步保存
     */
    CompletableFuture<Void> saveAsync(Cache<K, V> cache) throws PersistenceException;

    /**
     * 清理持久化文件
     */
    void cleanup() throws PersistenceException;

    /**
     * 检查持久化文件是否存在
     */
    boolean exists();
}
