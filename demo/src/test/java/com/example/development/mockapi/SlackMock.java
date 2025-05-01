package com.example.development.mockapi;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Slack Webhook エンドポイントのモック
 */
public class SlackMock implements HttpHandler {
    private static final String LOG_PREFIX = ">>> ";
    public static final Pattern uri = Pattern.compile("^/slack/services/([^/]+)/([^/]+)/([^/]+)$");

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        Matcher matcher = uri.matcher(path);
        if (!matcher.matches()) {
            exchange.sendResponseHeaders(404, -1);
            exchange.close();
            return;
        }

        // パスパラメータ
        String workspaceId = matcher.group(1);
        String applicationId = matcher.group(2);
        String token = matcher.group(3);

        // リクエストボディ
        InputStream stream = exchange.getRequestBody();
        String body = new String(stream.readAllBytes(), StandardCharsets.UTF_8);

        System.out.println(LOG_PREFIX + String.format("Request -> workspaceId=%s, applicationId=%s, token=%s, body=%s",
                workspaceId, applicationId, token, body));

        exchange.sendResponseHeaders(200, -1);
        exchange.close();

    }

}
