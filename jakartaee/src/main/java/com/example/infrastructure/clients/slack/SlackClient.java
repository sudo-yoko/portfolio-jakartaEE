package com.example.infrastructure.clients.slack;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import com.example.ApplicationProperties;
import com.example.infrastructure.clients.ProxyClient;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@Stateless
public class SlackClient {
    private static final Logger logger = Logger.getLogger(SlackClient.class.getName());
    private static final String LOG_PREFIX = ">>> [" + SlackClient.class.getSimpleName() + "]: ";

    @Inject
    private ProxyClient client;

    public void postMessage(String message) {
        String webhookUrl = ApplicationProperties.get("slack.webhook.url");

        JsonObject body = Json.createObjectBuilder().add("text", message).build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(webhookUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString(), StandardCharsets.UTF_8))
                .build();

        logger.info(LOG_PREFIX + String.format("Request(Outbound) -> webhookUrl=%s, message=%s", webhookUrl, message));

        HttpResponse<String> response;
        try {
            response = client.getClient().send(request, BodyHandlers.ofString());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        logger.info(LOG_PREFIX + String.format("response(Inbound) -> status=%s", response.statusCode()));
        if (response.statusCode() != 200) {
            throw new RuntimeException(String.format("Slack Webhook endpoint returned error status. status=%s, body=%s",
                    response.statusCode(), response.body()));
        }
    }
}
