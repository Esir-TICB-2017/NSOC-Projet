package bacnet;

import com.google.api.client.util.Sleeper;
import database.ReadInDatabase;
import indicators.Indicator;
import indicators.Indicators;
import org.json.JSONObject;
import sensor.sensorClass.Sensor;
import sensor.sensorClass.Sensors;

import java.util.*;

/**
 * Created by Loulou on 07/02/2017.
 */

/**
 * This class is just a way to fill database with fake sensors values
 */
public class AutoFillDB {
	public static void main(String[] args) throws Exception {
		double value = 0;
		Random r = new Random();
		// Create fake sensors instance
		List<Sensor> sensors = new ArrayList();
		ArrayList<JSONObject> sensorsList = ReadInDatabase.getAllSensors();
		for (JSONObject sensorAttributes : sensorsList) {
			Sensor sensor = new Sensor(sensorAttributes.getString("name"), sensorAttributes.getInt("id"), sensorAttributes.getString("unit"), sensorAttributes.getInt("bacnetId"), sensorAttributes.getBoolean("status"));
			sensors.add(sensor);
		}
		//Fill database with normal distributed values
		while (true) {
			for (Sensor sensor : sensors) {
				switch (sensor.getType()) {
					case "consumption":
						value = r.nextGaussian() * 1000 + 2000;
						sensor.setNewValue(value);
						break;
					case "co2":
						value = r.nextGaussian() * 400 + 600;
						if (value > 2000) {
							value = 2000;
						}
						if (value < 0) {
							value = 0;
						}
						sensor.setNewValue(value);

						break;
					case "production":
						value = r.nextGaussian() * 1000 + 2000;
						sensor.setNewValue(value);
						break;
					case "humidity":
						value = r.nextGaussian() * 10 + 40;
						if (value > 100) {
							value = 100;
						}
						if (value < 0) {
							value = 0;
						}
						sensor.setNewValue(value);
						break;
					case "temperature":
						value = r.nextGaussian() * 2 + 20;
						sensor.setNewValue(value);
						break;
					default:
						break;
				}
			}
			try {
				//Fill DB every 10 minutes
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
