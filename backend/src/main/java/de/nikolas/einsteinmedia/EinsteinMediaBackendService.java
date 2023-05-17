package de.nikolas.einsteinmedia;

import de.nikolas.einsteinmedia.commons.httpserver.http.server.HttpConfig;
import de.nikolas.einsteinmedia.commons.httpserver.http.server.HttpServer;
import de.nikolas.einsteinmedia.commons.httpserver.utils.Providers;
import de.nikolas.einsteinmedia.livechat.WebSocketServer;

/**
 * @author Nikolas Rummel
 * @since 08.05.23
 */
public class EinsteinMediaBackendService {

    public static void main(String[] args) {

        Providers.put(HttpConfig.class, new HttpConfig().withPort(8081));

        Thread webSocketThread = new Thread(() -> {
            try {
                WebSocketServer webSocket = new WebSocketServer();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Thread httpServerThread = new Thread(() -> {
            HttpServer server = new HttpServer();
            server.start();
        });

        webSocketThread.start();
        httpServerThread.start();
    }
}
