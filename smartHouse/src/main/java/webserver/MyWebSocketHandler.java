package webserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import computeAggregatedData.Indicators;
import database.ReadInDatabase;
import database.data.DataLinkToDate;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.JSONObject;

import javax.json.Json;

@WebSocket
public class MyWebSocketHandler {
    private final static HashMap<String, MyWebSocketHandler> sockets = new HashMap<>();
    public Session session;
    private String myUniqueId;

    private String getMyUniqueId() {
        // unique ID from this class' hash code
        return Integer.toHexString(this.hashCode());
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        this.session = session;

        // this unique ID

        try {
            ArrayList<DataLinkToDate> result;
            result = ReadInDatabase.getAllLastValues();
            Gson gson = new Gson();
            JSONObject last = new JSONObject();
            last.put("data",result);
            String strLast = last.toString();
            System.out.println(strLast);
            session.getRemote().sendString(strLast);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    }
}