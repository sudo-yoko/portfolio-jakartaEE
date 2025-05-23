package com.example.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiFunction;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.application.ExtractingJsonSerializerTestUtil.Artifact;
import com.example.application.ExtractingJsonSerializerTestUtil.POM;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

class ExtractingJsonSerializerTest {
    static final String LOG_PREFIX = "[TEST] " + ExtractingJsonSerializerTest.class.getSimpleName() + ": ";
    static Jsonb jsonb;

    @BeforeAll
    static void init() {
        JsonbConfig config = new JsonbConfig().withFormatting(true);
        jsonb = JsonbBuilder.create(config);
    }

    @BeforeEach
    void setup() {

    }

    // mvn -Dtest=ExtractingJsonSerializerTest#testFlatJson test
    @Test
    void testFlatJson() {
        // フラット構造のJavaオブジェクトを作成
        Artifact artifact = new Artifact();
        artifact.setGroupId("com.example");
        artifact.setArtifactId("demo");
        artifact.setVersion("1.0-SNAPSHOT");

        // 抽出するプロパティ
        String properties = "artifactId,version,test";

        JsonObject jsonObj = ExtractingJsonSerializer.properties(properties).apply(artifact);
        System.out.println(LOG_PREFIX + "result ->");
        System.out.println(jsonb.toJson(jsonObj));
    }

    // mvn -Dtest=ExtractingJsonSerializerTest#testNestedJson test
    @Test
    void testNestedJson() {
        // 入れ子構造のJavaオブジェクトを作成
        POM pom = new POM();

        Artifact artifact = new Artifact();
        artifact.setGroupId("com.example");
        artifact.setArtifactId("demo");
        artifact.setVersion("1.0-SNAPSHOT");
        pom.setArtifact(artifact);

        pom.setSource(11);
        pom.setTarget(11);

        List<Artifact> dependencies = new ArrayList<>();
        Artifact dependency1 = new Artifact();
        dependency1.setGroupId("jakarta.platform");
        dependency1.setArtifactId("jakarta-jakartaee-api");
        dependency1.setVersion("10.0.0");
        dependencies.add(dependency1);

        Artifact dependency2 = new Artifact();
        dependency2.setGroupId("org.eclipse.persistence");
        dependency2.setArtifactId("org.eclipse.persistence.jpa");
        dependency2.setVersion("4.0.2");
        dependencies.add(dependency2);
        pom.setDependencies(dependencies);

        // 抽出するプロパティ
        String properties = "artifact";

        JsonObject jsonObj = ExtractingJsonSerializer.properties(properties)
                .customJsonProcessor(jsonProcessor)
                .apply(pom);

        System.out.println(LOG_PREFIX + "result -> ");
        System.out.println(jsonb.toJson(jsonObj));
    }

    /**
     * 入れ子構造のJSONオブジェクトを処理する関数
     */
    private static BiFunction<Set<String>, JsonObject, JsonObject> jsonProcessor = (props, jsonObj) -> {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        for (Entry<String, JsonValue> entry : jsonObj.entrySet()) {
            if ("artifact".equals(entry.getKey())) {
                JsonObject artifact = entry.getValue().asJsonObject();
                JsonObject extracted = extract(props, artifact);
                if (!extracted.isEmpty()) {
                    builder.add(entry.getKey(), extracted);
                }
            } else if ("dependencies".equals(entry.getKey())) {
                JsonArray dependencies = entry.getValue().asJsonArray();
                JsonArray extracted = extract(props, dependencies);
                if (!extracted.isEmpty()) {
                    builder.add(entry.getKey(), extracted);
                }
            } else {
                if (props.contains(entry.getKey())) {
                    builder.add(entry.getKey(), entry.getValue());
                }
            }
        }
        return builder.build();
    };

    /**
     * JSONオブジェクトの配列から、指定されたプロパティを抽出する
     */
    private static JsonArray extract(Set<String> props, JsonArray arr) {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        arr.getValuesAs(JsonObject.class).forEach(obj -> {
            JsonObject extracted = extract(props, obj);
            if (!extracted.isEmpty()) {
                builder.add(extracted);
            }
        });
        return builder.build();
    }

    /**
     * JSONオブジェクトから、指定されたプロパティを抽出する
     */
    private static JsonObject extract(Set<String> props, JsonObject obj) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        obj.keySet().forEach(key -> {
            if (props.contains(key)) {
                builder.add(key, obj.get(key));
            }
        });
        return builder.build();
    }
}
