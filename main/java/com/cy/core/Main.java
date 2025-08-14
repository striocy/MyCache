package com.cy.core;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Server myServer = new Server(7777);
        myServer.start();
    }
}
