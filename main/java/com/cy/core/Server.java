package com.cy.core;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
public class Server {
    private final JCache<Integer,String> cache = new JCache(5);
    private final int port;
    public Server(int port) {
        this.port = port;
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
