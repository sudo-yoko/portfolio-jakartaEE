import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

// java MockApiServer.java
public class MockApiServer {
    private static final String LOG_PREFIX = String.format(">>> [%s]: ", MockApiServer.class.getSimpleName());
    private static final int PORT = 8080;

    public static void main(String args[]) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/users", new UsersHandler());
        server.setExecutor(null);
        server.start();

        System.out.println("Mock API Server started at http://localhost:" + PORT);
        System.out.println("Press Ctrl+C to stop the server.");
    }

    static class UsersHandler implements HttpHandler {
        private static final Pattern pattern = Pattern.compile("^/users/(\\d+)");

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            final String method = exchange.getRequestMethod();
            final String path = exchange.getRequestURI().getPath();
            System.out.println(LOG_PREFIX + String.format("Request -> method=%s, path=%s,", method, path));

            Matcher matcher = pattern.matcher(path);
            String userId = matcher.matches() ? matcher.group(1) : "";

            if (userId.isBlank()) {
                switch (exchange.getRequestMethod()) {
                    case "GET":
                        getUsers(exchange);
                        break;
                    default:
                        Response.MethodNotAllowed(exchange);
                }
            } else {
                switch (exchange.getRequestMethod()) {
                    case "GET":
                        getUser(exchange, userId);
                        break;
                    case "POST":
                        postUser(exchange, userId);
                        break;
                    default:
                        Response.MethodNotAllowed(exchange);
                }
            }
        }

        /**
         * GET /users
         */
        private void getUsers(HttpExchange exchange) {
            List<Map<String, String>> users = new ArrayList<>();
            Map<String, String> user;

            user = new HashMap<>();
            user.put("userId", "00001");
            user.put("userName", "test taro");
            users.add(user);

            user = new HashMap<>();
            user.put("userId", "00002");
            user.put("userName", "test jiro");
            users.add(user);

            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append(arr("users", users));
            sb.append("}");
            String response = sb.toString();

            Response.Ok(exchange, response);
        }

        /**
         * GET /users/{userId}
         */
        private void getUser(HttpExchange exchange, String userId) {
            String query = exchange.getRequestURI().getQuery();
            Map<String, String> queryParams = parseQuery(query);
            String option = queryParams.get("option");

            System.out.println(LOG_PREFIX
                    + String.format("Request -> query=%s, option=%s", queryParams, option));

            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append(q("userId")).append(":").append(q(userId)).append(",");
            sb.append(q("userName")).append(":").append(q("test taro"));
            sb.append("}");
            String response = sb.toString();

            Response.Ok(exchange, response);
        }

        public void postUser(HttpExchange exchange, String userId) {
            try {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                System.out.println(LOG_PREFIX + String.format("Request -> body=%s", body));
                Response.Created(exchange);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private static class Response {
        private static void Ok(HttpExchange exchange, String body) {
            System.out.println(LOG_PREFIX + String.format("Response -> %s", body));
            try {
                exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
                byte[] responseBytes = body.getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(200, responseBytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(responseBytes);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private static void Created(HttpExchange exchange) {
            try {
                // TODO レスポンスヘッダにLocation
                exchange.sendResponseHeaders(201, -1);
                exchange.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private static void NotFound(HttpExchange exchange) {
            try {
                exchange.sendResponseHeaders(404, -1);
                exchange.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private static void MethodNotAllowed(HttpExchange exchange) {
            try {
                exchange.sendResponseHeaders(405, -1);
                exchange.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static Map<String, String> parseQuery(String query) {
        Map<String, String> map = new HashMap<>();
        if (query == null)
            return map;
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length == 2) {
                map.put(pair[0], pair[1]);
            }
        }
        return map;
    }

    private static String q(String value) {
        final String QUOTE = "\""; // ダブルクォーテーションのエスケープ
        return QUOTE + value + QUOTE;
    }

    /**
     * JSON配列を作成
     */
    private static String arr(String key, List<Map<String, String>> recodes) {
        StringBuilder sb = new StringBuilder();
        sb.append(q(key)).append(":").append("[");
        for (Map<String, String> recode : recodes) {
            sb.append("{");
            for (Entry<String, String> entry : recode.entrySet()) {
                sb.append(q(entry.getKey())).append(":").append(q(entry.getValue())).append(",");
            }
            if (!recode.isEmpty()) {
                sb.deleteCharAt(sb.length() - 1); // 最後のカンマを除去
            }
            sb.append("}").append(",");
        }
        if (!recodes.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1); // 最後のカンマを除去
        }
        sb.append("]");
        return sb.toString();
    }
}
