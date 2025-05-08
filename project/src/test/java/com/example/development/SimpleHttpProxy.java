package com.example.development;

import java.io.IOException;

import org.eclipse.jetty.proxy.ConnectHandler;
import org.eclipse.jetty.server.Server;

public class SimpleHttpProxy {
    private static final String LOG_PREFIX = ">>> ";
    private static final int PORT = 9999;
    private Server server;

    public void startup() {
        try {
            server = new Server(PORT);
            ConnectHandler proxy = new ConnectHandler();
            server.setHandler(proxy);
            server.start();
            System.out.println(LOG_PREFIX + "Proxy Server started on http://localhost:" + PORT);
            server.join();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() {
        try {
            server.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
