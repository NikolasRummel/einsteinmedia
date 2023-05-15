package de.nikolas.einsteinmedia;

import de.nikolas.einsteinmedia.commons.httpserver.http.server.HttpConfig;
import de.nikolas.einsteinmedia.commons.httpserver.http.server.HttpServer;
import de.nikolas.einsteinmedia.commons.httpserver.utils.Providers;

/**
 * @author Nikolas Rummel
 * @since 08.05.23
 */
public class EinsteinMediaRestService {

    public static void main(String[] args) {
        Providers.put(HttpConfig.class, new HttpConfig().withPort(8081));
        HttpServer server = new HttpServer();
        server.start();
    }
}
