package de.nikolas.einsteinmedia.commons.httpserver.http.server;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Nikolas Rummel
 * @since 05.10.2021
 */

@AllArgsConstructor
@Data
public class HttpConfig {

    private int port;
    private String urlExt;
    private int masterThreads;
    private int workerThreads;
    private boolean debugMode;

    public HttpConfig() {
        this.port = 8082;
        this.masterThreads = 2;
        this.workerThreads = 8;
        this.urlExt = "";
        this.debugMode = false;
    }

    public HttpConfig withPort(int port) {
        this.port = port;
        return this;
    }
}
