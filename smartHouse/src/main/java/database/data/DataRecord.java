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
	private Timestamp date;
	private String type; // indicator or sensor
	private String name; //e.g : co2, humidity, etc

	public DataRecord(Double data, Timestamp date, String type, String name) {
		this.data = data;
		this.date = date;
		this.type = type;
		this.name = name;
	}

	public void setData(Double data) {
		this.data = data;
	}

	public void setDate(Timestamp date) {
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

	public Timestamp getDate() {
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
	public JsonElement toJsonElement() {
		JsonObject jsonElement = new JsonObject();
		jsonElement.addProperty("data", this.data);
		jsonElement.addProperty("date", this.date.toString());
		if(type != null) {
			jsonElement.addProperty("type", this.type);
		}
		if(name != null) {
			jsonElement.addProperty("name", this.name);
		}
		return jsonElement;
	}
}
