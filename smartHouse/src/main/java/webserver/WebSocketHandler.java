package webserver;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import computeAggregatedData.Indicators;
import database.Database;
import database.ReadInDatabase;
import database.data.DataLinkToDate;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.JSONArray;
import org.json.JSONObject;
import sensor.sensorClass.Sensor;
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
            JsonElement sensorValues = Sensors.getInstance().getLastValues();
            JsonArray sensors = sensorValues.getAsJsonArray();
            Double globalIndicator = ReadInDatabase.getLastIndicator("global").getData();
            JSONObject houseIndicator = new JSONObject();
            houseIndicator.put("key", Indicators.GLOBAL);
            houseIndicator.put("value", globalIndicator);
            Gson gson = new Gson();
            JsonElement result = gson.toJsonTree(houseIndicator).getAsJsonObject().get("map");
            sensors.add(result);
            String str = sensors.toString();
            session.getRemote().sendString(str);
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