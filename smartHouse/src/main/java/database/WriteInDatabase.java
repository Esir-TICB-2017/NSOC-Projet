package database;

import database.data.DataRecord;
import database.databaseInterface.InterfaceWriteDatabase;
import indicators.Indicator;
import org.json.JSONArray;
import org.json.JSONObject;
import sensor.sensorClass.Sensor;
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

	public static void setSensorStatus(Integer id, Boolean status) {
		Connection connection = ConnectionManager.getConnection();
		String sql = "UPDATE sensor_type SET status = ? WHERE id = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			preparedStatement.setBoolean(2, status);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
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

	public static void resetUserSettings(String userId) {
		Connection connection = ConnectionManager.getConnection();
		String sql = "String sql = INSERT INTO users_settings " +
				"(userid, setting_id, value) " +
				"SELECT ?, id, default_value " +
				"FROM settings";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, userId);
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

	public static void setDefaultSettings(Integer userId) {

	}

	public static void writeUserSettings(String userId, JSONObject settings) {

		Connection connection = ConnectionManager.getConnection();
		String sql = "IF EXISTS (SELECT * FROM user_settings WHERE userid=? AND setting_id=?) " +
				"UPDATE users_settings SET(value=?) WHERE userid=? " +
				"ELSE " +
				"INSERT INTO users_settings(userid, setting_id, value) VALUES(?, ?, ?) ";

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);

			int setting_id = (int) settings.get("id");
			String value = settings.get("value").toString();

			preparedStatement.setString(1, userId);
			preparedStatement.setInt(2, setting_id);
			preparedStatement.setString(3, value);
			preparedStatement.setString(4, userId);
			preparedStatement.setString(5, userId);
			preparedStatement.setInt(6, setting_id);
			preparedStatement.setString(7, value);

			//query execution
			preparedStatement.executeQuery();

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}
