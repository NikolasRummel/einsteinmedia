package de.nikolas.einsteinmedia.livechat;

import de.nikolas.einsteinmedia.commons.httpserver.log.Logger;
import de.nikolas.einsteinmedia.repository.PostsRepository;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.javax.server.config.JavaxWebSocketServletContainerInitializer;

import javax.websocket.Session;
import java.util.ArrayList;

public class WebSocketServer {

    private final Logger logger;
    private final Server server;
    private final ServerConnector connector;

    public static final ArrayList<Session> SESSIONS = new ArrayList<>();

    public WebSocketServer() throws Exception {
        this.logger = Logger.Factory.createLogger(PostsRepository.class);
        this.logger.info("Created WebSocketClass");

        server = new Server(8082);
        connector = new ServerConnector(server);
        server.addConnector(connector);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        JavaxWebSocketServletContainerInitializer.configure(context, (servletContext, wsContainer) -> {
            this.logger.info("add endpoint");
            wsContainer.setAsyncSendTimeout(5*60*1000);
            wsContainer.setDefaultMaxSessionIdleTimeout(5*60*1000);
            wsContainer.setDefaultMaxTextMessageBufferSize(65535);
            wsContainer.addEndpoint(WebSocketEndpoint.class);
        });

        server.start();
        server.join();
    }
}
