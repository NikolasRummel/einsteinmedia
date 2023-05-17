package de.nikolas.einsteinmedia.livechat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/chat")
public class WebSocketEndpoint {

    private final CountDownLatch closureLatch = new CountDownLatch(1);

    @OnOpen
    public void onWebSocketConnect(Session session) {
        System.out.println("Socket Connected: " + session.getId() + " current-connected: " + WebSocketServer.SESSIONS.size());
        WebSocketServer.SESSIONS.add(session);
        System.out.println("connected: " + WebSocketServer.SESSIONS.size());
    }

    @OnMessage
    public void onWebSocketText(Session session, String message) throws IOException {
        System.out.println("Received TEXT message: " + message);

        for (Session s : WebSocketServer.SESSIONS) {
            System.out.println(s.getId() + "." + message);
            if (!s.getId().equals(session.getId())) {
                s.getBasicRemote().sendText(message);
            }
        }
    }

    @OnClose
    public void onWebSocketClose(CloseReason reason) {
        System.out.println("Socket Closed: " + reason);
        closureLatch.countDown();
    }

    @OnError
    public void onWebSocketError(Throwable cause) {
        cause.printStackTrace(System.err);
    }

    public void awaitClosure() throws InterruptedException {
        System.out.println("Awaiting closure from remote");
        closureLatch.await();
    }
}
