package com.example.development;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.example.development.mockapi.SlackMock;
import com.sun.net.httpserver.HttpServer;

public class MockApiServer {
    private static final String LOG_PREFIX = ">>> ";
    private static final int PORT = 9000;
    private HttpServer server;

    public void startup() {
        try {
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
            server.createContext("/slack/services", new SlackMock());
            server.setExecutor(null);
            server.start();
            System.out.println(LOG_PREFIX + "Mock API Server started on http://localhost:" + PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() {
        server.stop(0);
    }
}
