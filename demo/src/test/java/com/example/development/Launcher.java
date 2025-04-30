package com.example.development;

// mvn test-compile exec:java -Dexec.mainClass=com.example.development.Launcher -Dexec.classpathScope=test
public class Launcher {
    public static void main(String args[]) {
        EmbeddedGlassFishServer app = new EmbeddedGlassFishServer("demo", 8080, 8081);
        app.startup();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            app.shutdown();
        }));
    }
}
