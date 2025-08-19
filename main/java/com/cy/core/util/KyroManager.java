package com.cy.core.util;

import com.cy.core.model.CircleNode;
import com.cy.core.model.Node;
import com.cy.core.model.RDBData;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.util.DefaultInstantiatorStrategy;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class KyroManager {
    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(()->{
        Kryo kryo = new Kryo();

        // 配置Kryo
        kryo.setReferences(true);
        kryo.setRegistrationRequired(false);
        kryo.setInstantiatorStrategy(new DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));

        // 注册常用类型
        kryo.register(HashMap.class);
        kryo.register(ConcurrentHashMap.class);
        kryo.register(ArrayList.class);
        kryo.register(LinkedList.class);
        kryo.register(String.class);
        kryo.register(Integer.class);
        kryo.register(Long.class);
        kryo.register(Double.class);
        kryo.register(Float.class);
        kryo.register(Boolean.class);
        kryo.register(Date.class);
        kryo.register(RDBData.class);
        kryo.register(Node.class);
        kryo.register(CircleNode.class);

        return kryo;
    });

    public static Kryo getKryo() {
        return kryoThreadLocal.get();
    }

}
