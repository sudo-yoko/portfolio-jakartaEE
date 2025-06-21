package com.example.infrastructure.clients;

import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.http.HttpClient;
import java.util.logging.Logger;

import com.example.Properties;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * プロキシ設定済みのHTTPクライアントの提供
 */
@ApplicationScoped
public class ProxyClient {
    private static final Logger logger = Logger.getLogger(ProxyClient.class.getName());
    private static final String LOG_PREFIX = ">>> [" + ProxyClient.class.getSimpleName() + "]: ";
    private HttpClient client;

    @PostConstruct
    private void init() {
        HttpClient.Builder builder = HttpClient.newBuilder();

        String host = Properties.get("proxy.host");
        int port = Properties.getInt("proxy.port");
        logger.info(LOG_PREFIX + String.format("Proxy host: %s, Proxy port: %s", host, port));

        InetSocketAddress proxyAddress = new InetSocketAddress(host, port);
        builder.proxy(ProxySelector.of(proxyAddress));

        client = builder.build();
    }

    public HttpClient getClient() {
        return client;
    }
}
