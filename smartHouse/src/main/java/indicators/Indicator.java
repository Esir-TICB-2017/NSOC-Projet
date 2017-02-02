package indicators;

import database.ReadInDatabase;
import sensor.sensorClass.Sensor;

import java.lang.reflect.Array;

/**
 * Created by Loulou on 02/02/2017.
 */
public class Indicator {
	private Integer id;
	private String type;
	private Double currentValue;
	private Double minComfortValue;
	private Double maxComfortValue;
	private Double minValue;
	private Double maxValue;


	public Indicator(Integer id, String type) {
		this.id = id;
		this.type = type;
		this.currentValue = null;
//		Double[] comfortValues = ReadInDatabase.getIndicatorComfortValue(id);
//		this.minComfortValue = comfortValues[0];
//		this.minComfortValue = comfortValues[1];
	}

	public Indicator(Integer id, String type, Double minComfortValue, Double maxComfortValue) {
		this(id, type);
		this.minComfortValue = minComfortValue;
		this.maxComfortValue = maxComfortValue;
	}

	public String getType() {
		return type;
	}

	public Double getCurrentValue() {
		return currentValue;
	}

	public Double calculateIndicator(Sensor sensor) {
		Double indicator = null;
		Double currentSensorValue = sensor.getCurrentValue();

		if (currentSensorValue > minComfortValue && currentSensorValue < maxComfortValue) {
			indicator = 100.0;
		} else {
			if (currentSensorValue > minValue && currentSensorValue < minComfortValue) {
				Double x = (currentSensorValue * 100) / minComfortValue;
				indicator = Math.log(x);
			} else {
				if (currentSensorValue > maxComfortValue) {
					Double x = (currentSensorValue * 100) / maxComfortValue;
					indicator = -Math.log(x);
				} else {
					indicator = currentValue;
				}
			}
		}
		return indicator;
	}
}
