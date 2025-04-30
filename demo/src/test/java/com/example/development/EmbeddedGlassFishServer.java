package com.example.development;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

import org.glassfish.embeddable.Deployer;
import org.glassfish.embeddable.GlassFish;
import org.glassfish.embeddable.GlassFishException;
import org.glassfish.embeddable.GlassFishProperties;
import org.glassfish.embeddable.GlassFishRuntime;
import org.glassfish.embeddable.archive.ScatteredArchive;

public class EmbeddedGlassFishServer {
    private static final String LOG_PREFIX = ">>> ";

    private final String appName;
    private final int httpPort;
    private final int httpsPort;

    private GlassFish glassfish;
    private Deployer deployer;
    private ScatteredArchive archive;

    public EmbeddedGlassFishServer(String appName, int httpPort, int httpsPort) {
        this.appName = appName;
        this.httpPort = httpPort;
        this.httpsPort = httpsPort;
    }

    public EmbeddedGlassFishServer() {
        this.appName = "EmbeddedGlassFishServer";

        // 空いているポートを割り当て
        try (ServerSocket socket = new ServerSocket(0)) {
            this.httpPort = socket.getLocalPort();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (ServerSocket socket = new ServerSocket(0)) {
            this.httpsPort = socket.getLocalPort();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startup() {
        try {
            start();
            deploy();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println(LOG_PREFIX + String.format(
                    "Embedded GlassFish Server started on HTTP port %s and HTTPS port %s. [application: %s]", httpPort,
                    httpsPort, appName));
        }
    }

    private void start() throws GlassFishException {
        GlassFishProperties properties = new GlassFishProperties();
        properties.setPort("http-listener", httpPort);
        properties.setPort("https-listener", httpsPort);
        glassfish = GlassFishRuntime.bootstrap().newGlassFish(properties);
        glassfish.start();
    }

    private void deploy() throws GlassFishException, IOException {
        deployer = glassfish.getDeployer();
        archive = new ScatteredArchive(appName, ScatteredArchive.Type.WAR, new File("src/main/webapp"));
        archive.addClassPath(new File("target", "classes"));
        archive.addClassPath(new File("target", "test-classes"));
        archive.addClassPath(new File("src/test/resources"));
        deployer.deploy(archive.toURI(), "--contextroot=" + appName);
    }

    public void shutdown() {
        try {
            undeploy();
            stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println(LOG_PREFIX + "Embedded GlassFish Server stopped.");
        }
    }

    private void undeploy() throws GlassFishException {
        deployer.undeploy(appName);
    }

    private void stop() throws GlassFishException {
        glassfish.stop();
    }
}
