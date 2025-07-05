package com.example.infrastructure.clients.slack;

import jakarta.ejb.Asynchronous;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class SlackClientAsyncAdapter {

    @Inject
    private SlackClient slackClient;

    @Asynchronous
    public void postMessageAsync(String message) {
        slackClient.postMessage(message);
    }
}
