package sensor.sensorClass;

import webserver.ConnectedClients;

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
	private List<Sensor> sensors = new ArrayList<>();;

	public List<Sensor> getSensors() {
		return sensors;
	}

	public void addSensor(Sensor sensor) {
		sensors.add(sensor);
	}
	public void removeSensors(Sensor sensor) {
		this.sensors.remove(sensor);
	}
}
