package sensor.sensorClass;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import database.ReadInDatabase;
import database.data.DataRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by loulou on 31/01/2017.
 */
public class Sensors {
	private static final Sensors INSTANCE = new Sensors();

	public static Sensors getInstance() {
		return INSTANCE;
	}

	private List<Sensor> sensors = new ArrayList();

	public List<Sensor> getSensors() {
		return sensors;
	}

	public Sensor getSensorByString(String type) {
		Sensor sensorResult = null;
		for (Sensor sensor : sensors) {
			if (sensor.getType().equals(type)) {
				sensorResult = sensor;
			}
		}
		return sensorResult;
	}

	public JsonElement getLastValues() {
		ArrayList<DataRecord> result = ReadInDatabase.getLastSensorsValues();
		Gson gson = new Gson();
		JsonElement resultsToJson = gson.toJsonTree(result);
		return resultsToJson;
	}

	public void addSensor(Sensor sensor) {
		sensors.add(sensor);
	}

	public void removeSensor(Sensor sensor) {
		sensors.remove(sensor);
	}
}
