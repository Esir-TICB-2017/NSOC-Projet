package database.data;

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

}
