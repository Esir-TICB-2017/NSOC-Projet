package database;

import database.data.DataRecord;
import database.databaseInterface.InterfaceWriteDatabase;
import indicators.Indicator;
import javafx.beans.binding.BooleanBinding;
import sensor.sensorClass.Sensor;
import webserver.ConnectedClients;

import javax.json.Json;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by loulou on 21/01/2017.
 */
public class WriteInDatabase extends Database implements InterfaceWriteDatabase {

	public static void writeSensorValue(Sensor sensor, Double value) {

		Connection connection = ConnectionManager.getConnection();
		Timestamp currentDate = getCurrentTimeStamp();
		String sql = "INSERT INTO sensors_data "
				+ "(sensor_value, submission_date, sensor_type_id) VALUES"
				+ "(?, ?, ?)";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDouble(1, value);
			preparedStatement.setTimestamp(2, currentDate);

			preparedStatement.setInt(3, sensor.getId());
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DatabaseEventsHandler.broadcastValue(new DataRecord(value, currentDate, "sensor", sensor.getType()));

	}

	public static void writeIndicatorValue(Indicator indicator, Double value) {
		Connection connection = ConnectionManager.getConnection();
		Timestamp currentDate = getCurrentTimeStamp();
		String sql = "INSERT INTO indicators "
				+ "(indicator_value, submission_date, indicator_type_id) VALUES"
				+ "(?, ?, ?)";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDouble(1, value);
			preparedStatement.setTimestamp(2, currentDate);
			preparedStatement.setInt(3, indicator.getId());
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DatabaseEventsHandler.broadcastValue(new DataRecord(value, currentDate, "indicator", indicator.getType()));
	}

	public static void deleteUserSession(String userId) {

		Connection connection = ConnectionManager.getConnection();
		String sql = "DELETE FROM sessions WHERE userid=?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, userId);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void createUser(String userId, String name, String surname, String email, String age) {
		Connection connection = ConnectionManager.getConnection();
		String sql = "INSERT INTO users (userid, name, surname, email, age) VALUES (?, ?, ?, ?, ?)";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, userId);
			preparedStatement.setString(2, name);
			preparedStatement.setString(3, surname);
			preparedStatement.setString(4, email);
			preparedStatement.setString(4, age);
			preparedStatement.executeQuery();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void updateUser(String userId, String name, String surname, String email, String age) {
		Connection connection = ConnectionManager.getConnection();
		String sql = "UPDATE users SET name = ?, surname = ?, email = ?, age = ? WHERE userid = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, surname);
			preparedStatement.setString(3, email);
			preparedStatement.setString(4, age);
			preparedStatement.setString(5, userId);
			preparedStatement.executeQuery();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void deleteUser(String userId) {
		Connection connection = ConnectionManager.getConnection();
		String sql = "DELETE FROM users WHERE userid = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, userId);
			preparedStatement.executeQuery();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void storeNewSession(String token, String userId, Timestamp expirationDate) {
		Connection connection = ConnectionManager.getConnection();
		String sql = "INSERT INTO sessions (userid, token, expiration_date) VALUES (?, ?, ?)";
		String updateSql = "UPDATE sessions SET userid = ? , token = ?, expiration_date = ? WHERE userid = ?";
		try {
			ArrayList<HashMap> userInfo = ReadInDatabase.getUserSession(userId);
			if (!userInfo.isEmpty()) {
				try {
					PreparedStatement preparedStatement = connection.prepareStatement(updateSql);
					preparedStatement.setString(1, userId);
					preparedStatement.setString(2, token);
					preparedStatement.setTimestamp(3, expirationDate);
					preparedStatement.setString(4, userId);
					preparedStatement.executeUpdate();
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				try {


					PreparedStatement preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setString(1, userId);
					preparedStatement.setString(2, token);
					preparedStatement.setTimestamp(3, expirationDate);
					preparedStatement.executeUpdate();
					preparedStatement.close();

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
	}



	public static void writeSettings(String userId, ArrayList<String> settings) {

		Connection connection = ConnectionManager.getConnection();
		String sql = "INSERT INTO settings(userid, settemp, setco2min, setco2max, setconsobj, setprodobj, tempind, humidityind, co2ind, consind, prodind, perhome, globalchart, tempchart, humiditychart, co2chart, conschart, prodchart, perdata) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
            //values attribution with type parsing
			PreparedStatement preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setString(1, userId); //set user id

            //step settings
			preparedStatement.setDouble(2, Double.parseDouble(settings.get(0)));	//setTempStep
			preparedStatement.setDouble(3, Double.parseDouble(settings.get(1)));	//setCo2MinStep
			preparedStatement.setDouble(4, Double.parseDouble(settings.get(2)));	//setCo2MaxStep
			preparedStatement.setDouble(5, Double.parseDouble(settings.get(3)));	//setConsObj
			preparedStatement.setDouble(6, Double.parseDouble(settings.get(4)));	//setProdObj

            //home page settings
			preparedStatement.setBoolean(7, Boolean.parseBoolean(settings.get(5)));		//displayTemperatureIndOnHome
			preparedStatement.setBoolean(8, Boolean.parseBoolean(settings.get(6)));		//displayHumidityIndOnHome
			preparedStatement.setBoolean(9, Boolean.parseBoolean(settings.get(7)));		//displayCo2IndOnHome
			preparedStatement.setBoolean(10, Boolean.parseBoolean(settings.get(8)));		//displayConsumptionIndOnHome
			preparedStatement.setBoolean(11, Boolean.parseBoolean(settings.get(9)));		//displayProductionIndOnHome
			preparedStatement.setInt(12, Integer.parseInt(settings.get(10)));	            //periodChartOnHome

            //data page settings
			preparedStatement.setBoolean(13, Boolean.parseBoolean(settings.get(11)));	//displayGlobalChartOnData
			preparedStatement.setBoolean(14, Boolean.parseBoolean(settings.get(12)));	//displayTemperatureChartOnData
			preparedStatement.setBoolean(15, Boolean.parseBoolean(settings.get(13)));	//displayHumidityChartOnData
			preparedStatement.setBoolean(16, Boolean.parseBoolean(settings.get(14)));	//displayCo2ChartOnData
			preparedStatement.setBoolean(17, Boolean.parseBoolean(settings.get(15)));	//displayConsumptionChartOnData
			preparedStatement.setBoolean(18, Boolean.parseBoolean(settings.get(16)));	//displayProductionChartOnData
			preparedStatement.setInt(19, Integer.parseInt(settings.get(17)));	        //periodChartsOnData

            //query execution
			preparedStatement.executeQuery();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
