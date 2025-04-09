package com.example;

import java.util.List;

public class ExtractingJsonSerializerTestUtil {

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
}
