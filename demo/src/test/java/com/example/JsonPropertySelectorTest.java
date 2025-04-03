package com.example;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.json.JsonObject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

class JsonPropertySelectorTest {
    static final String LOG_PREFIX = "[TEST] " + JsonPropertySelectorTest.class.getSimpleName() + ": ";
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
        JsonObject jsonObj = JsonPropertySelector.properties("artifactId,version,test").apply(artifact);
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

        JsonObject jsonObj = JsonPropertySelector.properties("version").apply(pom);
        System.out.println(LOG_PREFIX + "result -> ");
        System.out.println(jsonb.toJson(jsonObj));
    }

    /**
     * フラット構造のJavaオブジェクト
     */
    public static class Artifact {
        String groupId;
        String artifactId;
        String version;

        public Artifact() {
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getArtifactId() {
            return artifactId;
        }

        public void setArtifactId(String artifactId) {
            this.artifactId = artifactId;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }

    /**
     * 入れ子構造のJavaオブジェクト
     * (POM風オブジェクト)
     */
    public static class POM {

        private Artifact artifact;
        private Integer source;
        private Integer target;
        private List<Artifact> dependencies;

        public POM() {
        }

        public Artifact getArtifact() {
            return artifact;
        }

        public void setArtifact(Artifact artifact) {
            this.artifact = artifact;
        }

        public Integer getSource() {
            return source;
        }

        public void setSource(Integer source) {
            this.source = source;
        }

        public Integer getTarget() {
            return target;
        }

        public void setTarget(Integer target) {
            this.target = target;
        }

        public List<Artifact> getDependencies() {
            return dependencies;
        }

        public void setDependencies(List<Artifact> dependencies) {
            this.dependencies = dependencies;
        }
    }

    private static BiFunction<Set<String>, JsonObject, JsonObject> jsonProcessor = (props, jsonObj) -> {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        for (Entry<String, JsonValue> entry : jsonObj.entrySet()) {
            if (entry.getValue().gatValueType == JsonValue.ValueType.ARRAY) {

            }
        }
    };
}
