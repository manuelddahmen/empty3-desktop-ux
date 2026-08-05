package one.empty3.apps.opad.server;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class HealthCheckServer {
    private final int port;
    private HttpServer server;

    public HealthCheckServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", exchange -> {
            String response = "OK";
            exchange.sendResponseHeaders(200, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        });
        server.setExecutor(null);
        server.start();
        System.out.println("Health check server listening on port " + port);
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            System.out.println("Health check server stopped");
        }
    }
}
