package com.example.development;

import java.io.IOException;
import java.util.logging.Logger;

import org.eclipse.jetty.proxy.ConnectHandler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 簡易HTTPプロキシサーバー
 */
public class SimpleHttpProxy {
    private static final Logger logger = Logger.getLogger(SimpleHttpProxy.class.getName());
    private static final String LOG_PREFIX = ">>> [" + SimpleHttpProxy.class.getSimpleName() + "]: ";
    private static final String CONSOLE_PREFIX = ">>> ";

    private static final int PORT = 9999;
    private Server server;

    public void startup() {
        try {
            server = new Server(PORT);
            ConnectHandler proxy = new ConnectHandler();
            proxy.setHandler(new AbstractHandler() {
                @Override
                public void handle(String target, Request baseRequest, HttpServletRequest request,
                        HttpServletResponse response) throws IOException, ServletException {
                    logger.info(LOG_PREFIX + String.format("Received request -> method=%s, uri=%s", request.getMethod(),
                            request.getRequestURI()));

                }
            });
            server.setHandler(proxy);
            server.start();
            System.out.println(CONSOLE_PREFIX + "Proxy Server started on http://localhost:" + PORT);
            server.join();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() {
        try {
            server.stop();
            server.destroy();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
