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
	private String unit;
	private Integer id;
	private Integer bacnetId;
	private Boolean status;

	public int getId() {
		return id;
	}


	public Sensor(String type, Integer id, String unit, Integer bacnetId, Boolean status) {
		this.type = type;
		this.id = id;
		this.unit = unit;
		this.bacnetId = bacnetId;
		this.status = status;
	}

	public Integer getBacnetId() {
		return bacnetId;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public String getUnit() {
		return unit;
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
