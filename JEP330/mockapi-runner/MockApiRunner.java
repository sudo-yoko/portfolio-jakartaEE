import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
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

// java MockApiRunner.java
// curl -i http://localhost:8080/users/12345?test1=aaa&test2=ccc
public class MockApiRunner {
    private static final String LOG_PREFIX = String.format(">>> [%s]: ", MockApiRunner.class.getSimpleName());
    private static final int PORT = 8080;

    public static void main(String args[]) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/users", new Users());
        server.setExecutor(null);
        server.start();
        System.out.println("Mock API Server started at http://localhost:" + PORT);
        System.out.println("Press Ctrl+C to stop the server.");
    }

    /**
     * /users/{userId}
     */
    static class Users implements HttpHandler {
        private static final Pattern pattern = Pattern.compile("^/users/(\\d+)");

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            final String path = exchange.getRequestURI().getPath();

            System.out.println(
                    LOG_PREFIX + String.format("Request -> method=%s, path=%s", exchange.getRequestMethod(), path));

            Matcher matcher = pattern.matcher(path);
            String userId = matcher.matches() ? matcher.group(1) : "";

            // /users
            if (userId.isBlank()) {
                exchange.sendResponseHeaders(404, -1);
                exchange.close();
                // 一覧を返す
            }

            // /users/{userId}
            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                getUser(exchange, userId);

            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
                exchange.close();
            }

            switch (exchange.getRequestMethod()) {
                case "GET":
                    getUser(exchange, userId);
                    break;
                case "POST":
                    // postUser(exchange);
                    break;
                default:
                    exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }

        }

        /**
         * GET /users/{userId}?option=xxx
         * RESPONSE { "userId": "12345", "userName": "test taro" }
         * 
         * curl -i http://localhost:8080/users/12345?test1=aaa&test2=ccc
         * 
         * @throws IOException
         */
        private void getUser(HttpExchange exchange, String userId) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            Map<String, String> queryParams = parseQuery(query);
            String option = queryParams.get("option");
            System.out.println(
                    LOG_PREFIX + String.format("Request -> query=%s, test1=%s", queryParams, queryParams.get("test1")));

            // response
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append(q("userId")).append(":").append(q(userId)).append(",");
            sb.append(q("userName")).append(":").append(q("test taro"));
            sb.append("}");
            String response = sb.toString();

            System.out.println(LOG_PREFIX + String.format("Response -> %s", response));

            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            // UTF-8 エンコーディングは getBytes(StandardCharsets.UTF_8) のように明示するとより堅牢です。
            exchange.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
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

    private static StringBuilder jsonArray(String propertyName, List<Map<String, String>> data) {
        StringBuilder sb = new StringBuilder();
        sb.append(q(propertyName)).append(":").append("[");
        for (Map<String, String> d : data) {
            sb.append("{");
            for (Entry<String, String> entry : d.entrySet()) {
                sb.append(q(entry.getKey())).append(":").append(q(entry.getValue())).append(",");
            }
            sb.append("},");
        }
        sb.append("]");

        return sb;
    }

}
