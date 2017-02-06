package sensor.sensorClass;

import database.ReadInDatabase;
import database.WriteInDatabase;
import database.data.DataLinkToDate;

import java.sql.Timestamp;
import java.util.ArrayList;


/**
 * Created by loulou on 21/01/2017.
 */
public class Sensor {
	private double currentValue;
	private String type;
	private Integer id;

	public int getId() {
		return id;
	}


	public Sensor(String type, Integer id) {
		this.type = type;
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setCurrentValue(double currentValue) {
		this.currentValue = currentValue;
	}

	public double getCurrentValue() {
		return currentValue;
	}

	public DataLinkToDate getLastValue() {
		return ReadInDatabase.getLastValue(this);
	}

	public ArrayList<DataLinkToDate> getValuesOnPeriod(Timestamp startDate, Timestamp endDate) {
		return ReadInDatabase.getValuesOnPeriod(this, startDate, endDate);
	}

	public void setNewValue(double newValue) {
		if (newValue != currentValue) {
//			WriteInDatabase.writeSensorValue(this, newValue);
			this.setCurrentValue(newValue);
		}
	}
}
