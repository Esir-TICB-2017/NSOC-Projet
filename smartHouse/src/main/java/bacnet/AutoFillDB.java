package bacnet;

import com.google.api.client.util.Sleeper;
import database.ReadInDatabase;
import indicators.Indicator;
import indicators.Indicators;
import sensor.sensorClass.Sensor;
import sensor.sensorClass.Sensors;

import java.util.*;

/**
 * Created by Loulou on 07/02/2017.
 */
public class AutoFillDB {
	public static void main(String[] args) throws Exception {
		double value = 0;
		Random r = new Random();
		List<Sensor> sensors = new ArrayList();
		Map<String, Integer> sensorsList = ReadInDatabase.getAllSensorsName();
		Iterator its = sensorsList.entrySet().iterator();
		while (its.hasNext()) {
			Map.Entry pair = (Map.Entry) its.next();
			Sensor sensor = new Sensor((String) pair.getKey(), (Integer) pair.getValue());
			sensors.add(sensor);
		}
		//                 connection();
		try {
			// getTemperature();
		} catch (Exception e) {
			e.printStackTrace();
		}
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

//				Indicator indicator = Indicators.getInstance().getIndicatorByString(sensor.getType());
//				indicator.calculateIndicator();
			}
//                        try {
//                            value = getTemperature();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        ts.setNewValue(value);
			try {
				Thread.sleep(1000 * 60 * 10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

            /*} catch (BACnetException e) {
				// TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("Problème de récupération de la valeur !");
            }*/

	}
}
