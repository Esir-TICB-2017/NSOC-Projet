package database;

import com.google.gson.Gson;
import indicators.Indicator;
import org.json.JSONObject;
import sensor.sensorClass.Sensor;
import webserver.ConnectedClients;

import javax.json.Json;

/**
 * Created by loulou on 21/01/2017.
 */
public class DatabaseEventsHandler {
    public static void broadcastSensorValue (Sensor sensor, Double value) {
       String type = sensor.getType();
        JSONObject result = new JSONObject();
        result.put("key", type);
        result.put("value", value);
        String str = result.toString();
        ConnectedClients.getInstance().writeAllMembers(str);
    }

    public static void broadcastIndicatorValue (Indicator indicator, Double value) {
        JSONObject result = new JSONObject();
        result.put(indicator.getType(),value);
        String str = result.toString();
        ConnectedClients.getInstance().writeAllMembers(str);
    }
}
