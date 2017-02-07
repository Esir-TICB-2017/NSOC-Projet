package webserver;

import java.io.IOException;
import java.util.HashMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import database.ReadInDatabase;
import database.data.DataRecord;
import indicators.Indicator;
import indicators.Indicators;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import sensor.sensorClass.Sensors;

@WebSocket
public class WebSocketHandler {
    private final static HashMap<String, WebSocketHandler> sockets = new HashMap<>();
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
            JsonElement lastValues = Sensors.getInstance().getLastValues();
            Indicator indicator = Indicators.getInstance().getIndicatorByString("global");
            DataRecord lastRecord = indicator.getLastRecord();

            JsonObject toSend = new JsonObject();
            toSend.add("globalIndicator", lastRecord.toJsonElement());
            toSend.add("lastValues", lastValues);
            String toSendString = toSend.toString();
            System.out.println(toSendString);
            session.getRemote().sendString(toSendString);
            // session.setIdleTimeout(5 * 60 * 1000);

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