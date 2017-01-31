package webserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
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
import org.json.JSONObject;
import sensor.sensorClass.Sensor;
import sensor.sensorClass.Sensors;

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
            List<Sensor> sensors = Sensors.getInstance().getSensors();
            for(Sensor sensor : sensors) {
                Double value = ReadInDatabase.getLastValue(sensor).getData();
                String type = Database.getTypeFromSensorClass(sensor);
                JSONObject result = new JSONObject();
                result.put("key", type);
                result.put("value", value);
                String str = result.toString();
                session.getRemote().sendString(str);
            }
            DataLinkToDate globalIndicator = ReadInDatabase.getLastIndicator(Indicators.GLOBAL);
            JSONObject result = new JSONObject();
            result.put("key", Indicators.GLOBAL);
            result.put("value", globalIndicator);
            String str = result.toString();
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