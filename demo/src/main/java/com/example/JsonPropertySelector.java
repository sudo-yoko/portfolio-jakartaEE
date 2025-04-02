package com.example;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

/**
 * JavaオブジェクトをJSONシリアライズして、指定されたプロパティのみを含む新しいJSONオブジェクトを構築する。<br>
 * デフォルトはフラット構造のJSONに対応。配列構造や入れ子構造の場合は、専用の処理関数を呼び元で作成し、<br>
 * customJsonProcessorにセットする。
 */
public class JsonPropertySelector {
    private static final Jsonb jsonb = JsonbBuilder.create();

    private Set<String> props;

    /**
     * このクラスはファクトリーメソッドを通じてのみインスタンス化させるため、<br>
     * コンストラクタはプライベートに制限しています。
     */
    private JsonPropertySelector() {

    }

    /**
     * 抽出するプロパティ名を設定する
     * 
     * @param properties 抽出するプロパティ名。複数ある場合はカンマで区切る
     * @return 自身のインスタンスを返すため、メソッドチェーンが可能
     */
    public static JsonPropertySelector properties(String properties) {
        JsonPropertySelector instance = new JsonPropertySelector();
        if (properties == null || properties.isBlank()) {
            return instance;
        }
        // プロパティをカンマで分割してSetに格納する
        Set<String> props = Arrays.stream(properties.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toSet());
        instance.props = props;
        return instance;
    }

    public JsonObject apply(Object target) {
        if (target == null) {
            return null;
        }

        // JavaオブジェクトをJSON文字列に変換
        String jsonString = jsonb.toJson(target);

        // JSON文字列をJSONオブジェクトに変換
        JsonObject jsonObj;
        try (JsonReader reader = Json.createReader(new StringReader(jsonString))) {
            jsonObj = reader.readObject();
        }

        // プロパティが指定されていない場合は、もとのJavaオブジェクトのJSONオブジェクトを返す
        if (props == null || props.isEmpty()) {
            return jsonObj;
        }

        // 指定されたプロパティのみを含む新しいJSONオブジェクトを構築する
        JsonObject filteredJson = processJson(jsonObj);
        return filteredJson;
    }

    /**
     * JSONを処理する
     */
    private JsonObject processJson(JsonObject jsonObj) {
        if (this.customJsonProcessor != null) {
            return this.customJsonProcessor.apply(props, jsonObj);
        }
        return defaultJsonProcessor.apply(props, jsonObj);
    }

    /**
     * デフォルトのJSON処理関数。
     * フラット構造のJSONに対応
     */
    private BiFunction<Set<String>, JsonObject, JsonObject> defaultJsonProcessor = (props, jsonObj) -> {
        for (String prop : props) {
            if (!jsonObj.containsKey(prop)) {
                System.out.println("未定義のプロパティが指定されています。");
            }
        }
        JsonObjectBuilder builder = Json.createObjectBuilder();
        for (String key : jsonObj.keySet()) {
            if (props.contains(key)) {
                builder.add(key, jsonObj.get(key));
            }
        }
        return builder.build();
    };

    /**
     * カスタムのJSON処理関数。
     */
    private BiFunction<Set<String>, JsonObject, JsonObject> customJsonProcessor;

    /**
     * カスタムのJSON処理関数を設定する
     * 自身のインスタンスを返すため、メソッドチェーンが可能
     */
    public JsonPropertySelector customJsonProcessor(
            BiFunction<Set<String>, JsonObject, JsonObject> customJsonProcessor) {
        this.customJsonProcessor = customJsonProcessor;
        return this;
    }
}
