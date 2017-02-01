package sensor.sensorClass;

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
			if (sensor.getType() == type) {
				sensorResult = sensor;
			}
		}
		return sensorResult;
	}

	public void addSensor(Sensor sensor) {
		sensors.add(sensor);
	}

	public void removeSensors(Sensor sensor) {
		sensors.remove(sensor);
	}
}
