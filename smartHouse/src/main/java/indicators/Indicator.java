package indicators;

import database.ReadInDatabase;
import sensor.sensorClass.Sensor;

import java.lang.reflect.Array;
import java.util.ArrayList;

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


	public Indicator(String type, Integer id) {
		this.id = id;
		this.type = type;
		this.currentValue = null;
		ArrayList<Double> comfortValues = ReadInDatabase.getIndicatorValues(id);
		this.minComfortValue = comfortValues.get(0);
		this.maxComfortValue = comfortValues.get(1);
		this.minValue = comfortValues.get(2);
		this.maxValue = comfortValues.get(3);
	}

	public Indicator(Integer id, String type, Double minComfortValue, Double maxComfortValue) {
		this(type, id);
		this.minComfortValue = minComfortValue;
		this.maxComfortValue = maxComfortValue;
		ArrayList<Double> comfortValues = ReadInDatabase.getIndicatorValues(id);
		this.minValue = comfortValues.get(2);
		this.maxValue = comfortValues.get(3);
	}

	public String getType() {
		return type;
	}

	public Double getCurrentValue() {
		return currentValue;
	}

	public Double calculateIndicator(Sensor sensor) {
		Double indicator;
		Double currentSensorValue = sensor.getCurrentValue();
		System.out.println(minComfortValue);
		System.out.println(maxComfortValue);
		System.out.println(currentSensorValue);
		if (currentSensorValue > minComfortValue && currentSensorValue < maxComfortValue) {
			indicator = 100.0;
		} else {
			if (currentSensorValue > minValue && currentSensorValue < minComfortValue) {
				Double x = (currentSensorValue * 100) / maxComfortValue;
				indicator = x;
			} else {
				if (currentSensorValue > maxComfortValue) {
					Double x = (currentSensorValue * 100) / minComfortValue;
					indicator = x;
				} else {
					indicator = currentValue;
				}
			}
		}
		System.out.println("indicator " + sensor.getType() + " " + indicator);
		return indicator;
	}
}
