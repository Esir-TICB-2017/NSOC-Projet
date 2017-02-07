package indicators;

import database.ReadInDatabase;
import database.WriteInDatabase;
import database.data.DataLinkToDate;
import sensor.sensorClass.Sensor;
import sensor.sensorClass.Sensors;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

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
	public int getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public Double getCurrentValue() {
		if(currentValue != null) {
			return currentValue;
		}else {
			return getLastValue();
		}
	}
	public DataLinkToDate getLastRecord() {
		return ReadInDatabase.getLastIndicator(this);
	}

	public Double getLastValue() {
		return getLastRecord().getData();
	}

	public void setCurrentValue(Double currentValue) {
		this.currentValue = currentValue;
	}

	public Double calculateIndicator() {
		Double indicator;
		if(!this.getType().equals("global")){
			Sensor sensor = Sensors.getInstance().getSensorByString(this.getType());
			Double currentSensorValue = sensor.getCurrentValue();
			if (currentSensorValue > minComfortValue && currentSensorValue < maxComfortValue) {
				indicator = 100.0;
			} else {
				if (currentSensorValue > minValue && currentSensorValue < minComfortValue) {
					Double x = (currentSensorValue * 100) / minComfortValue;
					indicator = x;
				} else {
					if (currentSensorValue > maxComfortValue && currentSensorValue < maxValue) {
						Double x = (currentSensorValue * 100) / maxComfortValue;
						indicator = 200 - x;
						if(indicator < 0) {
							indicator = 0.0;
						}
					} else {
						indicator = getCurrentValue();
					}
				}
			}
		} else {
			indicator = calculateGlobalIndicator();
		}

		this.setCurrentValue(indicator);
		WriteInDatabase.writeIndicatorValue(this, indicator);
		return indicator;
	}

	private  Double calculateGlobalIndicator() {
		Double globalIndicator;
		List<Indicator> indicators;
		indicators = Indicators.getInstance().getIndicators();
		int length = indicators.size();
		Double sum = 0.0;
		for(Indicator indicator : indicators){
			if(!indicator.getType().equals("global")) {
				sum += indicator.getCurrentValue();
			}
		}
		globalIndicator = sum / length;
		return globalIndicator;
	}
}
