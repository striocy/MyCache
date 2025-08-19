package com.cy.core;
import com.cy.api.Cache;
import com.cy.core.cacheEntry.JCache;
import com.cy.core.exception.PersistenceException;

import java.io.*;
import java.net.Socket;
public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final Cache mycache;
    public ClientHandler(Socket clientSocket, Cache mycache) {
        this.clientSocket = clientSocket;
        this.mycache = mycache;
    }
    private String executeCommand(String[] command) throws FileNotFoundException, PersistenceException {
        if (command.length == 0) return "ERR empty command\r\n";
        String cmd = command[0].toUpperCase();
        switch(cmd){
            case "SET" :
                if(command.length != 4) return "ERR set command\r\n";
                return mycache.set(command[1], command[2], Long.valueOf(command[3]));

            case "GET" :
                if(command.length != 2) return "ERR get command\r\n";
                return mycache.get(command[1])+ "\r\n";

            case "CONTAINS" :
                if(command.length != 2) return "ERR contains command\r\n";
                return mycache.contains(command[1])+"\r\n";

            case "DELETE":
                if(command.length != 2) return "ERR delete command\r\n";
                return mycache.remove(command[1]);

            case "EXPIRE":
                if(command.length != 3) return "ERR expire command\r\n";
                mycache.expire(command[1], Long.valueOf(command[2]));
                return "OK\r\n";

            case "SAVE":
                return mycache.save();

            case "LOAD":
                return mycache.load();

        }
        return "error, unknown command\r\n";
    }

    @Override
    public void run() {
        try(
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))
        ){
            String line;
            writer.write("welcome to cyCache!");
            writer.flush();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                System.out.println(line);
                System.out.println(parts.length);
                // 解析并执行命令
                writer.write(executeCommand(parts));
                writer.flush();
        }}catch (IOException e){
                System.err.println("Client connection error: " + e.getMessage());
            } catch (PersistenceException e) {
            throw new RuntimeException(e);
        }
    }
}
