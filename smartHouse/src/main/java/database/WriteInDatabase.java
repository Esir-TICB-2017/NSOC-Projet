package database;

import database.databaseInterface.InterfaceWriteDatabase;
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

	public static void writeSensorValue(Sensor sensor, double value) {
		System.out.println(sensor.getClass().getSimpleName() + " " + value);

		Connection connection = ConnectionManager.getConnection();
		String className = sensor.getType();
		String sql = "INSERT INTO sensors_data "
				+ "(sensor_value, submission_date, sensor_type_id) VALUES"
				+ "(?, ?, ?)";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDouble(1, value);
			preparedStatement.setTimestamp(2, getCurrentTimeStamp());
			preparedStatement.setInt(3, sensor.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DatabaseEventsHandler.broadcastSensorValue(sensor, value);

	}

	public static void writeIndicatorValue(String indicator, int value) {
		Connection connection = ConnectionManager.getConnection();
		String sql = "INSERT INTO indicators (VALUE, SUBMISSION_DATE) VALUES" + "(?, ?)";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, value);
			preparedStatement.setTimestamp(2, getCurrentTimeStamp());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DatabaseEventsHandler.broadcastIndicatorValue(indicator, value);
	}

	public static void deleteUserSession(String userId) {

		Connection connection = ConnectionManager.getConnection();
		String sql = "DELETE FROM sessions WHERE userid=?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, userId);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
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
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
