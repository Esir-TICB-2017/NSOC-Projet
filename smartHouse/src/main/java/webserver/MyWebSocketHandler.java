package webserver;

import java.io.IOException;
import java.util.HashMap;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import javax.json.Json;

@WebSocket
public class MyWebSocketHandler {
    private final static HashMap<String, MyWebSocketHandler> sockets = new HashMap<>();
    public Session session;
    private String myUniqueId;

    @OnWebSocketConnect
    public void onConnect(Session session) {
        this.session = session;
        ConnectedClients.getInstance().join(this);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        ConnectedClients.getInstance().part(this);
    }

    @OnWebSocketError
    public void onError(Throwable t) {
        System.out.println("Error: " + t.getMessage());
    }

    @OnWebSocketMessage
    public void onText(String message) {

        //ConnectedClients.getInstance().writeAllMembers("Hello all");
    }
}