package database;

import database.data.DataRecord;
import database.databaseInterface.InterfaceWriteDatabase;
import indicators.Indicator;
import org.json.JSONArray;
import org.json.JSONObject;
import sensor.sensorClass.Sensor;
import utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import static jdk.nashorn.internal.objects.NativeMath.round;

/**
 * Created by loulou on 21/01/2017.
 */
public class WriteInDatabase extends Database implements InterfaceWriteDatabase {

	public static void writeSensorValue(Sensor sensor, Double value) {

		Connection connection = ConnectionManager.getConnection();
		Timestamp currentDate = Utils.getCurrentTimeStamp();
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
			DatabaseEventsHandler.broadcastValue(new DataRecord(value, currentDate.getTime(), "sensor", sensor.getType()));
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void writeIndicatorValue(Indicator indicator, Double value) {
		double val  = Math.round(value*100);
		Double realVal = val/100;
		Connection connection = ConnectionManager.getConnection();
		Timestamp currentDate = Utils.getCurrentTimeStamp();
		String sql = "INSERT INTO indicators "
				+ "(indicator_value, submission_date, indicator_type_id) VALUES"
				+ "(?, ?, ?)";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDouble(1, realVal);
			preparedStatement.setTimestamp(2, currentDate);
			preparedStatement.setInt(3, indicator.getId());
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DatabaseEventsHandler.broadcastValue(new DataRecord(realVal, currentDate.getTime(), "indicator", indicator.getType()));
	}

	public static void setSensorStatus(Sensor sensor, Boolean status) {
		Connection connection = ConnectionManager.getConnection();
		String sql = "UPDATE sensor_type SET status = ? WHERE id = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, sensor.getId());
			preparedStatement.setBoolean(2, status);
			preparedStatement.executeUpdate();
			preparedStatement.close();
			DatabaseEventsHandler.broadcastStatus(sensor);
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
		String sql = "INSERT INTO users_settings(userid, setting_id, value) " +
				"SELECT ?, id, default_value FROM settings";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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

	public static void writeNewToken(String email, String token) {
		Connection connection = ConnectionManager.getConnection();
		String sql = "UPDATE users SET current_token = ? WHERE email = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, token);
			preparedStatement.setString(2, email);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void writeNewRole(JSONObject user) {
		Connection connection = ConnectionManager.getConnection();
		String sql = "UPDATE users SET role = ? WHERE email = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, user.getString("role"));
			preparedStatement.setString(2, user.getString("email"));
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void writeUserSetting(String userId, JSONObject settings) {

		Connection connection = ConnectionManager.getConnection();
		String sql = "INSERT INTO users_settings(userid, setting_id, value) " +
				"VALUES(?, ?, ?) " +
				"ON DUPLICATE KEY UPDATE userid = VALUES(userid), setting_id = VALUES(setting_id), value = VALUES(value)";

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);

			int setting_id = (int) settings.get("setting_id");
			String value = settings.get("value").toString();

			preparedStatement.setString(1, userId);
			preparedStatement.setInt(2, setting_id);
			preparedStatement.setString(3, value);

			//query execution after checking value
			if(isInteger(value)) {
				preparedStatement.executeUpdate();
			}
			else{
				ArrayList<String> allowedValues = ReadInDatabase.checkIfValueIsAllowed(setting_id);
				Boolean authorize = false;
				for (String valueToTest : allowedValues){
					if (valueToTest.equals(value)){
						authorize = true;
					}
				}
				if(authorize) {
					preparedStatement.executeUpdate();
				}
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	public static void deleteUser(JSONObject user) {
		Connection connection = ConnectionManager.getConnection();
		String sql = "DELETE  FROM users WHERE email = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, user.getString("email"));
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void addUser(JSONObject user) {
		Connection connection = ConnectionManager.getConnection();
		String sql = "INSERT INTO users (email, role) VALUES (?, ?)";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, user.getString("email"));
			preparedStatement.setString(2, user.getString("role"));
			preparedStatement.executeQuery();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch(NumberFormatException e) {
			return false;
		} catch(NullPointerException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}
}
