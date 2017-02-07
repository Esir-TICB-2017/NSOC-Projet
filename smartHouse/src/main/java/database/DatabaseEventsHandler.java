package database;

import com.google.gson.Gson;
import database.data.DataRecord;
import indicators.Indicator;
import org.json.JSONObject;
import sensor.sensorClass.Sensor;
import webserver.ConnectedClients;

import javax.json.Json;
import java.sql.Timestamp;

/**
 * Created by loulou on 21/01/2017.
 */
public class DatabaseEventsHandler {
    public static void broadcastValue (DataRecord record) {
        String str = record.toJsonElement().toString();
        ConnectedClients.getInstance().writeAllMembers(str);
    }
}
