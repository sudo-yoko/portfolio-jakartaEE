package com.example.development.mockapi;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Slack Webhook エンドポイントのモック
 * POST /slack/services/{workspaceId}/{applicationId}/{token}
 */
public class SlackMock implements HttpHandler {
    private static final Logger logger = Logger.getLogger(SlackMock.class.getName());
    private static final String LOG_PREFIX = ">>> [" + SlackMock.class.getSimpleName() + "]: ";

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
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
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

        logger.info(
                LOG_PREFIX + String.format("request(Inbound) -> workspaceId=%s, applicationId=%s, token=%s, body=%s",
                        workspaceId, applicationId, token, body));

        exchange.sendResponseHeaders(200, -1);
        exchange.close();
    }
}
