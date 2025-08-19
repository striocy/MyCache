package com.cy.core.persist;

import com.cy.api.Cache;
import com.cy.api.PersistenceManager;
import com.cy.core.exception.PersistenceException;
import com.cy.core.model.RDBData;
import com.cy.core.util.KyroManager;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RDB<K,V> implements PersistenceManager<K,V> {
    private final Kryo kryo = KyroManager.getKryo();
    private final File file;
    public RDB(File file) {
        this.file = file;
    }
    @Override
    public void save(Cache<K, V> cache) throws PersistenceException, FileNotFoundException {
        List<RDBData<K,V>> list = new ArrayList<>();
        Iterator<? extends RDBData<K,V>> it= cache.valueIterator();
        while (it.hasNext()) {
            list.add(it.next());
        }
        try (Output output = new Output(new FileOutputStream(file))) {
            // 将整个列表对象写入文件
            kryo.writeObject(output, list);
        }
    }

    @Override
    public Cache<K, V> load(Cache<K,V> cache) throws PersistenceException, FileNotFoundException {
        ArrayList<? extends RDBData<K,V>> list;
        try (Input input = new Input(new FileInputStream(file))) {
            // 从文件中读取整个列表对象
            // 注意：需要告诉 Kryo 我们期望读出的对象类型
            list = kryo.readObject(input, ArrayList.class);
            Iterator<? extends RDBData<K,V>> it= list.iterator();
            while(it.hasNext()){
                RDBData<K,V> data = it.next();
                cache.set(data.getKey(), data.getValue(),  data.getExpireAt());
            }
        }

        return cache;
    }


    @Override
    public CompletableFuture<Void> saveAsync(Cache<K, V> cache) throws PersistenceException {
        return null;
    }

    @Override
    public void cleanup() throws PersistenceException {

    }

    @Override
    public boolean exists() {
        return false;
    }
}
