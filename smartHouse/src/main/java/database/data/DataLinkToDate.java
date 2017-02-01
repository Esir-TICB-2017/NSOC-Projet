package database.data;

import com.google.gson.JsonObject;
import org.json.JSONObject;

import java.sql.Timestamp;

/**
 * Created by clement on 25/01/2017.
 */
public class DataLinkToDate {
	private double data;
	private Timestamp date;
	private String type;
	private String name;

	public DataLinkToDate(double data, Timestamp date, String type, String name) {
		this.data = data;
		this.date = date;
		this.type = type;
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public double getData() {
		return data;
	}

	public Timestamp getDate() {
		return date;
	}

	public JSONObject getDataToJson() {
		JSONObject json = new JSONObject();
		json.put("value", this.getData());
		json.put("type", this.getType());
		json.put("date", this.getDate());
		json.put("name", this.getName());

		return json;
	}
}
