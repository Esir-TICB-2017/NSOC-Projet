package sensor.sensorClass;

import database.ReadInDatabase;
import database.WriteInDatabase;
import database.data.DataRecord;

import java.sql.Timestamp;
import java.util.ArrayList;


/**
 * Created by loulou on 21/01/2017.
 */
public class Sensor {
	private Double currentValue;
	private String type;
	private Integer id;

	public int getId() {
		return id;
	}


	public Sensor(String type, Integer id) {
		this.type = type;
		this.id = id;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public DataRecord getLastRecord() {
		return ReadInDatabase.getLastValue(this);
	}

	public Double getLastValue() {
		return getLastRecord().getData();
	}

	public double getCurrentValue() {
		if (currentValue != null) {
			return currentValue;
		} else {
			return getLastValue();
		}
	}

	public ArrayList<DataRecord> getRecordsOnPeriod(Timestamp startDate, Timestamp endDate) {
		return ReadInDatabase.getValuesOnPeriod(getId(), startDate, endDate);
	}

	public void setNewValue(Double newValue) {
		if (newValue != this.currentValue) {
			WriteInDatabase.writeSensorValue(this, newValue);
			this.setCurrentValue(newValue);
		}
	}

	public void setCurrentValue(double currentValue) {
		this.currentValue = currentValue;
	}


}
