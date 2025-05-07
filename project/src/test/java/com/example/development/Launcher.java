package com.example.development;

import java.util.concurrent.CountDownLatch;

// mvn compile test-compile exec:java -Dexec.mainClass=com.example.development.Launcher -Dexec.classpathScope=test -Duser.timezone=Asia/Tokyo
public class Launcher {
    private static final String LOG_PREFIX = ">>> ";

    public static void main(String args[]) throws InterruptedException {
        EmbeddedGlassFishServer app = new EmbeddedGlassFishServer("project", 8080, 8081);
        app.startup();

        MockApiServer mock = new MockApiServer();
        mock.startup();

        CountDownLatch latch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            app.shutdown();
            mock.shutdown();
            latch.countDown();
        }));
        System.out.println(LOG_PREFIX + "終了するには Ctrl+C を押してください。");
        latch.await();
    }
}
