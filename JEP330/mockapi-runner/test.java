import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

// java test.java
public class test {

    public static void main(String args[]) {

        List<Map<String, String>> users = new ArrayList<>();

        /*
         * Map<String, String> user1 = new HashMap<>();
         * user1.put("userId", "00001");
         * user1.put("userName", "test taro");
         * users.add(user1);
         * 
         * Map<String, String> user2 = new HashMap<>();
         * user2.put("userId", "00002");
         * user2.put("userName", "test jiro");
         * users.add(user2);
         */

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(q("id")).append(":").append(q("aaaa")).append(",");
        sb.append(arr("user", users)).append(",");
        sb.append(q("timestamp")).append(":").append(q("20250621"));
        sb.append("}");

        System.out.println(sb.toString());

    }

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

    private static String q(String value) {
        final String QUOTE = "\""; // ダブルクォーテーションのエスケープ
        return QUOTE + (value == null ? "" : value) + QUOTE;
    }

}
