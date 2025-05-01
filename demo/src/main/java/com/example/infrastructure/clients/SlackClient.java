package com.example.infrastructure.clients;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@RequestScoped
public class SlackClient {

    @Inject
    private ProxyHttpClientProvider client;

    public void poseMessage(String webhookUrl, String message) {

        JsonObject body = Json.createObjectBuilder().add("text", message).build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(webhookUrl))
                .header("Content-Type", "application.json")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString(), StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response;
        try {
            response = client.getClient().send(request, BodyHandlers.ofString());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (200 != response.statusCode()) {
            throw new RuntimeException(String.format("Slack Webhook endpoint returned error status. status=%s, body=%s",
                    response.statusCode(), response.body()));
        }
    }
}
