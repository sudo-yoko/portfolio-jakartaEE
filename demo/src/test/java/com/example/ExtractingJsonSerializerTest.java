package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiFunction;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.ExtractingJsonSerializerTestUtil.Artifact;
import com.example.ExtractingJsonSerializerTestUtil.POM;

import jakarta.json.Json;
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

    @Test
    void testFlatJson() {
        // フラット構造のJavaオブジェクト
        Artifact artifact = new Artifact();
        artifact.setGroupId("com.example");
        artifact.setArtifactId("demo");
        artifact.setVersion("1.0-SNAPSHOT");
        JsonObject jsonObj = ExtractingJsonSerializer.properties("artifactId,version,test").apply(artifact);
        System.out.println(LOG_PREFIX + "result -> ");
        System.out.println(jsonb.toJson(jsonObj));
    }

    @Test
    void testNestedJson() {
        // 入れ子構造のJavaオブジェクト
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

        JsonObject jsonObj = ExtractingJsonSerializer.properties("version").apply(pom);
        System.out.println(LOG_PREFIX + "result -> ");
        System.out.println(jsonb.toJson(jsonObj));
    }

    /**
     * 入れ子構造のJavaオブジェクト
     */
    private static BiFunction<Set<String>, JsonObject, JsonObject> jsonProcessor = (props, jsonObj) -> {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        for (Entry<String, JsonValue> entry : jsonObj.entrySet()) {
            if (entry.getValue().getValueType() == JsonValue.ValueType.ARRAY) {

            }
        }
    };
}
