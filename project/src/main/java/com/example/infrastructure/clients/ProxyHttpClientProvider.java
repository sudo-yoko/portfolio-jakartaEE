package com.example.infrastructure.clients;

import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.http.HttpClient;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * プロキシ設定済みのHTTPクライアントの提供
 */
@ApplicationScoped
public class ProxyHttpClientProvider {
    private HttpClient client;

    @PostConstruct
    private void init() {
        HttpClient.Builder builder = HttpClient.newBuilder();

        String proxyHost = "xxxxx";
        int proxyPort = 0;
        InetSocketAddress proxyAddress = new InetSocketAddress(proxyHost, proxyPort);
        builder.proxy(ProxySelector.of(proxyAddress));

        client = builder.build();
    }

    public HttpClient getClient() {
        return client;
    }
}
