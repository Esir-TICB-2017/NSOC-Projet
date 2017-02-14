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
        String str = record.toJson().toString();
        ConnectedClients.getInstance().writeAllMembers(str);
    }

    public static void broadcastStatus(Sensor sensor) {
        JSONObject data = new JSONObject();
        DataRecord lastRecord = sensor.getLastRecord();
        data.put("name", sensor.getType());
        data.put("data", lastRecord.getData());
        data.put("date", lastRecord.getDate());
        data.put("type", sensor.getTypeOf());
        data.put("unit", sensor.getUnit());
        data.put("connected", sensor.getStatus());
        ConnectedClients.getInstance().writeAllMembers(data.toString());
    }
}
