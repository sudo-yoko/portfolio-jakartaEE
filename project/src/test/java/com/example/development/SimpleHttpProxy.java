package com.example.development;

import java.io.IOException;
import java.util.logging.Logger;

import org.eclipse.jetty.proxy.ProxyServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

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

            ServletContextHandler context = new ServletContextHandler();
            context.setContextPath("/");

            ServletHolder holder = new ServletHolder(new SimpleHttpProxyImpl());
            context.addServlet(holder, "/*");
            server.setHandler(context);

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

    private static class SimpleHttpProxyImpl extends ProxyServlet.Transparent {
        @Override
        public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
            // ログ出力
            HttpServletRequest req = (HttpServletRequest) request;
            logger.info(LOG_PREFIX
                    + String.format("Received request -> method=%s, uri=%s", req.getMethod(),
                            req.getRequestURI()));
            // デフォルトの転送処理
            super.service(request, response);
        }
    }
}
