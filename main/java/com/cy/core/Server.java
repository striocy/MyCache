package com.cy.core;
import com.cy.api.Cache;
import com.cy.core.cacheEntry.EvictCache;
import com.cy.core.cacheEntry.JCache;
import com.cy.core.evict.EvictClock;
import com.cy.core.evict.EvictFIFO;
import com.cy.core.evict.EvictLRU;
import com.cy.core.expire.CacheExpireSorted;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
public class Server {
    private final Cache<Integer,String> cache;
    private final int port;
    public Server(int port) {
        this.port = port;
        File file = new File("test.out");
        cache = new EvictCache<>(6, new EvictLRU<>(), new CacheExpireSorted<>(), file);
        cache.getExpirer().setCache(cache);
    }
    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Cache server started on port " + port);
        while (true) {
            System.out.println("Waiting for client on port " + port);
            Socket clientSocket = serverSocket.accept();
            System.out.println("Accepted connection from client on port " + port);
            new Thread(new ClientHandler(clientSocket, cache)).start();
        }
    }

}
