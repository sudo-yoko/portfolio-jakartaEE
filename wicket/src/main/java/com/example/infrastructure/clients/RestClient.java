package com.example.infrastructure.clients;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;

//@ApplicationScoped
public class RestClient {
    private Client client;

    //@PostConstruct
    private void init() {
        client = ClientBuilder.newClient();
    }

    //@PreDestroy
    private void teardown() {
        client.close();
    }

    public Client get() {
        return client;
    }
}
