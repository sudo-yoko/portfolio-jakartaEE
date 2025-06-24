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

// java MockApiServer2.java
public class MockApiServer2 {
    private static final String LOG_PREFIX = String.format(">>> [%s]: ", MockApiServer2.class.getSimpleName());
    private static final int PORT = 8080;

    /**
     * ルート定義
     */
    private static final List<Route> routes = List.of(
            new Route("GET", "^/users$", new GetUsersHandler()),
            new Route("GET", "^/users/(\\d+)$", new GetUserHandler()),
            new Route("POST", "^/users/(\\d+)$", new PostUserHandler()));

    public static void main(String args[]) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                final String method = exchange.getRequestMethod();
                final String path = exchange.getRequestURI().getPath();
                System.out.println(LOG_PREFIX + String.format("Request -> method=%s, path=%s,", method, path));

                for (Route route : routes) {
                    if (route.matches(method, path)) {
                        route.handler.handle(exchange, route.matcher);
                        return;
                    }
                }
                Response.NotFound(exchange);
            }
        });
        server.setExecutor(null);
        server.start();

        System.out.println("Mock API Server started at http://localhost:" + PORT);
        System.out.println("Press Ctrl+C to stop the server.");
    }

    private interface RequestHandler {
        void handle(HttpExchange exchange, Matcher pathMatcher);
    }

    /**
     * リクエストを処理するハンドラー
     * GET /users
     */
    public static class GetUsersHandler implements RequestHandler {
        @Override
        public void handle(HttpExchange exchange, Matcher pathMatcher) {
            List<Map<String, String>> users = new ArrayList<>();
            users.add(user("00001", "test taro"));
            users.add(user("00002", "test jiro"));
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append(arr("users", users));
            sb.append("}");
            String response = sb.toString();
            Response.Ok(exchange, response);
        }
    }

    /**
     * リクエストを処理するハンドラー
     * GET /users/{userId}
     */
    public static class GetUserHandler implements RequestHandler {
        @Override
        public void handle(HttpExchange exchange, Matcher pathMatcher) {
            String query = exchange.getRequestURI().getQuery();
            Map<String, String> queryParams = parseQuery(query);
            String option = queryParams.get("option");
            String userId = pathMatcher.group(1);
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
    }

    /**
     * リクエストを処理するハンドラー
     * POST /users/{userId}
     */
    public static class PostUserHandler implements RequestHandler {
        @Override
        public void handle(HttpExchange exchange, Matcher pathMatcher) {
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

    /**
     * ルート定義
     */
    public static class Route {
        final String method;
        final Pattern pattern;
        final RequestHandler handler;
        Matcher matcher;

        public Route(String method, String regexPath, RequestHandler handler) {
            this.method = method;
            this.pattern = Pattern.compile(regexPath);
            this.handler = handler;
        }

        public boolean matches(String method, String path) {
            if (!this.method.equalsIgnoreCase(method)) {
                return false;
            }
            Matcher matcher = pattern.matcher(path);
            if (!matcher.matches()) {
                return false;
            }
            this.matcher = matcher;
            return true;
        }
    }

    /////////////////////////
    // utilities
    /////////////////////////

    private static Map<String, String> parseQuery(String query) {
        Map<String, String> map = new HashMap<>();
        if (query == null) {
            return map;
        }
        String[] parts = query.split("&");
        if (parts.length == 2) {
            String[] pair = parts[1].split("=");
            if (pair.length == 2) {
                map.put(pair[0], pair[1]);
            }
        }
        return map;
    }

    private static String q(String value) {
        final String QUOTE = "\""; // ダブルクォーテーションのエスケープ
        return QUOTE + (value == null ? "" : value) + QUOTE;
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

    /**
     * ユーザーデータの作成
     */
    private static Map<String, String> user(String userId, String userName) {
        Map<String, String> user = new HashMap<>();
        user.put("userId", userId);
        user.put("userName", userName);
        return user;
    }
}
