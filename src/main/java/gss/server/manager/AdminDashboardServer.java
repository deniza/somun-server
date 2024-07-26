package gss.server.manager;

import gss.GssLogger;
import gss.server.model.DashboardStats;
import gss.server.util.Config;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.gson.Gson;

public class AdminDashboardServer {

    private static volatile AdminDashboardServer instance;

    private final int PORT = Config.getInt("admin_interface_port");
    private final String HOST = Config.getString("admin_interface_bind_host");
    private final String adminIndex = Config.getString("admin_interface_template");
    private final AtomicBoolean serverRunning = new AtomicBoolean(true);

    private final DashboardStats stats;

    private AdminDashboardServer() {
        // Prevent form the reflection api.
        if (instance != null) {
            throw new RuntimeException("Use get() method to get the single instance of this class.");
        }

        stats = new DashboardStats(0, 0, 0, "", "0.0.1");

    }

    public static AdminDashboardServer get() {
        // Double check locking pattern
        if (instance == null) {
            synchronized (AdminDashboardServer.class) {
                if (instance == null) {
                    instance = new AdminDashboardServer();
                }
            }
        }
        return instance;
    }

    public void start() {
        Undertow server = Undertow.builder()
                .addHttpListener(PORT, HOST)
                .setHandler(new HttpHandler() {
                    @Override
                    public void handleRequest(HttpServerExchange exchange) throws Exception {
                        String path = exchange.getRequestPath();
                        if (path.equals("/getStats")) {
                            handleGetStats(exchange);
                        } else if (path.equals("/shutdownServer")) {
                            handleShutdownServer(exchange);
                        } else if (path.equals("/")) {
                            handleAdminPage(exchange);
                        } else {
                            exchange.setStatusCode(404);
                            exchange.getResponseSender().send("Not Found");
                        }
                    }
                }).build();

        server.start();

        GssLogger.info("SOMUN Admin interface started at http://" + HOST + ":" + PORT);
    }

    public DashboardStats getStats() {
        return stats;
    }

    private void handleGetStats(HttpServerExchange exchange) {
        if (!serverRunning.get()) {
            exchange.setStatusCode(503);
            exchange.getResponseSender().send("Server is shutting down");
            return;
        }

        String jsonResponse = new Gson().toJson(stats.toMap());

        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        exchange.getResponseSender().send(jsonResponse);
    }

    private void handleShutdownServer(HttpServerExchange exchange) {
        if (!serverRunning.get()) {
            exchange.setStatusCode(503);
            exchange.getResponseSender().send("Server is already shutting down");
            return;
        }

        serverRunning.set(false);
        exchange.getResponseSender().send("Server is shutting down");

        GssLogger.info("Received shutdownn command from admin interface. Shutting down server.");
        System.exit(0);

    }

    private void handleAdminPage(HttpServerExchange exchange) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(adminIndex)));
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html");
        exchange.getResponseSender().send(content);
    }
}
