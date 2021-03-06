package database.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.util.converter.TimeStringConverter;
import org.json.JSONObject;

import java.sql.Timestamp;

/**
 * Created by clement on 25/01/2017.
 */

/**
 * This class is used to represent a specific data row returned from database.
 * The format of the data is [value][date][type][name]
 */
public class DataRecord {
	private Double data;
	private Long date;
	private String type; // indicator or sensor
	private String name; //e.g : co2, humidity, etc

	public DataRecord(Double data, Long date, String type, String name) {
		this.data = data;
		this.date = date;
		this.type = type;
		this.name = name;
	}

	public void setData(Double data) {
		this.data = data;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getData() {
		return data;
	}

	public Long getDate() {
		return date;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	/**
	 * this method is used to get a data record as a jsonObject, in order to send
	 * it to client
	 * @return jsonObject : {data: data, date: date, type: type, name: name}
	 */
	public JSONObject toJson() {
		JSONObject data = new JSONObject();
		data.put("data", this.getData());
		data.put("date", this.getDate());
		if(type != null) {
			data.put("type", this.getType());
		}
		if(name != null) {
			data.put("name", this.getName());
		}
		return data;
	}
}
