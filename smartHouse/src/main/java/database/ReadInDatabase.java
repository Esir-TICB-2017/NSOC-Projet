package database;

import com.google.gson.JsonObject;
import database.data.DataRecord;
import database.databaseInterface.InterfaceReadDatabase;
import indicators.Indicator;
import org.json.JSONArray;
import org.json.JSONObject;
import sensor.sensorClass.Sensor;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by loulou on 21/01/2017.
 */
public class ReadInDatabase extends Database implements InterfaceReadDatabase {
	// TODO : refactor function with Sensor in input
	public static DataRecord getLastValue(Sensor sensor) {
		double data;
		Long date;
		DataRecord result = null;
		String sensorType = sensor.getType();
		Connection connection = ConnectionManager.getConnection();
		String sql =
				"SELECT sensors_data.submission_date, sensors_data.sensor_value " +
						"FROM sensors_data " +
						"INNER JOIN sensor_type " +
						"ON sensors_data.sensor_type_id = sensor_type.id " +
						"WHERE sensor_type.type_name = ? " +
						"ORDER BY sensors_data.submission_date DESC LIMIT 1";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, sensorType);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					date = rs.getTimestamp("submission_date").getTime();
					data = rs.getDouble("sensor_value");
					result = new DataRecord(data, date, "sensor", sensorType);
				}
				rs.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			preparedStatement.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static ArrayList<JSONObject> getAllSensors() {
		Connection connection = ConnectionManager.getConnection();
		String sql = "SELECT type_name, id, unit, bacnet_id, status FROM sensor_type";
		ArrayList<JSONObject> sensorsList = new ArrayList<>();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					JSONObject sensor = new JSONObject();
					sensor.put("name", rs.getString("type_name"));
					sensor.put("id", rs.getInt("id"));
					sensor.put("unit", rs.getString("unit"));
					sensor.put("bacnetId", rs.getInt("bacnet_id"));
					sensor.put("status", rs.getBoolean("status"));
					sensorsList.add(sensor);
				}
				rs.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			preparedStatement.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return sensorsList;
	}

	public static ArrayList<JSONObject> getAllIndicators() {
		Connection connection = ConnectionManager.getConnection();
		String sql = "SELECT type_name, id FROM indicators_type";
		ArrayList<JSONObject> indicatorsList = new ArrayList<>();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					JSONObject indicator = new JSONObject();
					indicator.put("name", rs.getString("type_name"));
					indicator.put("id", rs.getInt("id"));
					indicatorsList.add(indicator);

				}
				rs.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			preparedStatement.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return indicatorsList;
	}

	public static ArrayList<DataRecord> getValuesOnPeriod(Integer sensorId, Timestamp startDate, Timestamp endDate) {
		Connection connection = ConnectionManager.getConnection();
		ArrayList<DataRecord> list = new ArrayList(1);
		String sql = "SELECT sensors_data.submission_date, sensors_data.sensor_value, sensor_type.type_name " +
				"FROM sensors_data INNER JOIN sensor_type " +
				"ON sensors_data.sensor_type_id=sensor_type.id " +
				"AND sensor_type.id = ? " +
				" WHERE sensors_data.submission_date BETWEEN ? AND ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, sensorId);
			preparedStatement.setTimestamp(2, startDate);
			preparedStatement.setTimestamp(3, endDate);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					Long date = rs.getTimestamp("submission_date").getTime();
					Double data = rs.getDouble("sensor_value");
					String name = rs.getString("type_name");
					list.add(new DataRecord(data, date, "sensor", name));
				}
				rs.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			preparedStatement.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return list;
	}

/*
	public static ArrayList<DataRecord> getAllLastIndicators() {

		ArrayList<DataRecord> list = new ArrayList(Indicators.ALL_INDICATORS.length);

		for (String indicator : Indicators.ALL_INDICATORS) {
			list.add(getLastIndicator(indicator));
		}
		return list;

	}
	*/

	public static ArrayList<DataRecord> getLastSensorsValues() {
		ArrayList<DataRecord> list = new ArrayList();
		Connection connection = ConnectionManager.getConnection();
		String sql = "SELECT sensors_data.submission_date, sensors_data.sensor_value, sensor_type.type_name " +
				"FROM sensors_data LEFT JOIN sensor_type " +
				"ON sensors_data.sensor_type_id=sensor_type.id " +
				"WHERE sensors_data.submission_date = (SELECT MAX(sensors_data.submission_date) FROM sensors_data WHERE sensors_data.sensor_type_id=sensor_type.id) " +
				"ORDER BY sensor_type.type_name";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					Long date = rs.getTimestamp("submission_date").getTime();
					Double data = rs.getDouble("sensor_value");
					String name = rs.getString("type_name");
					DataRecord row = new DataRecord(data, date, "sensor", name);
					list.add(row);
				}
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static JSONArray getSettings() {
		JSONArray settings = new JSONArray();
		Connection connection = ConnectionManager.getConnection();
		String sql ="SELECT settings.id, settings.order, settings.type, settings.default_value, settings.description, settings.name, settings.data_type, settings.min_value, settings.max_value, allowed_setting_value.item_value, allowed_setting_value.caption " +
		"FROM settings " +
		"LEFT JOIN allowed_setting_value ON settings.id = allowed_setting_value.setting_id";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					String name = rs.getString("name");
					String description = rs.getString(("description"));
					String dataType = rs.getString("data_type");
					Double minValue = rs.getDouble("min_value");
					Double maxValue = rs.getDouble("max_value");
					String defaultValue = rs.getString("default_value");
					String itemValue = rs.getString("item_value");
					String caption = rs.getString("caption");
					String type = rs.getString("type");
					int order = rs.getInt("order");
					Integer found = findSettingInJson(settings, name);
					if (found != -1) {
						JSONObject setting = settings.getJSONObject(found);
						JSONObject newItemValue = new JSONObject();
						newItemValue.put("itemValue", itemValue);
						newItemValue.put("caption", caption);
						setting.getJSONArray("allowedValues").put(newItemValue);
					} else {
						JSONObject setting = new JSONObject();
						setting.put("name", name);
						setting.put("description", description);
						setting.put("minValue", minValue);
						setting.put("maxValue", maxValue);
						setting.put("dataType", dataType);
						setting.put("defaultValue", defaultValue);
						setting.put("type", type);
						setting.put("order", order);
						JSONArray allowedValues = new JSONArray();
						JSONObject newItemValue = new JSONObject();
						newItemValue.put("itemValue", itemValue);
						newItemValue.put("caption", caption);
						allowedValues.put(newItemValue);
						setting.put("allowedValues", allowedValues);
						settings.put(setting);
					}
				}
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return settings;
	}

	public static JSONArray getUserSettings(String userId) {
		JSONArray userSettings = new JSONArray();
		Connection connection = ConnectionManager.getConnection();
		String sql = "SELECT setting_id, value FROM users_settings WHERE userid=?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, userId);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					int setting_id = rs.getInt("setting_id");
					String value = rs.getString("value");
					JSONObject userSetting = new JSONObject();
					userSetting.put("setting_id", setting_id);
					userSetting.put("value", value);
					userSettings.put(userSetting);
				}
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userSettings;
	}

	public static Integer findSettingInJson(JSONArray settings, String name) {
		for (int i = 0; i < settings.length(); ++i) {
			JSONObject setting = settings.getJSONObject(i);
			if (setting.get("name").equals(name)) {
				return i;
			}
		}
		return -1;
	}

	public static ArrayList<DataRecord> getLastIndicatorsValues() {
		ArrayList<DataRecord> list = new ArrayList();
		Connection connection = ConnectionManager.getConnection();
		String sql = "SELECT indicators.submission_date, indicators.sensor_value, indicators_type.type_name " +
				"FROM indicators LEFT JOIN indicators_type " +
				"ON indicators.indicator_type_id=indicators_type.id " +
				"WHERE indicators.submission_date = (SELECT MAX(indicators.submission_date) FROM indicators WHERE indicators.indicators_type_id=indicators_type.id) " +
				"ORDER BY indicators_type.type_name";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					Long date = rs.getTimestamp("submission_date").getTime();
					Double data = rs.getDouble("indicator_value");
					String name = rs.getString("type_name");
					DataRecord row = new DataRecord(data, date, "indicator", name);
					list.add(row);
				}
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static DataRecord getLastIndicator(Indicator indicator) {
		DataRecord result = null;
		double data;
		Long date;
		Connection connection = ConnectionManager.getConnection();
		String sql = "SELECT submission_date, indicator_value " +
				"FROM indicators " +
				"WHERE indicator_type_id = ? " +
				"ORDER BY submission_date DESC LIMIT 1";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, indicator.getId());
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					date = rs.getTimestamp("submission_date").getTime();
					data = rs.getDouble("indicator_value");
					result = new DataRecord(data, date, "indicator", indicator.getType());
				}
				rs.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			preparedStatement.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static Boolean checkExistingUser(String email) {
		Connection connection = ConnectionManager.getConnection();
		String sql = "SELECT email FROM users WHERE email=?";
		String result = null;
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, email);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					result = rs.getString("email");
				}
				rs.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			preparedStatement.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		if (email.equals(result)) {
			return true;
		} else {
			return false;
		}
	}

	public static String getCurrentToken(String email) {
		Connection connection = ConnectionManager.getConnection();
		String sql = "SELECT current_token FROM users WHERE email=?";
		String result = null;
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, email);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					result = rs.getString("current_token");
				}
				rs.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			preparedStatement.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static ArrayList<DataRecord> getIndicatorsOnPeriod(Indicator indicator, Timestamp startDate, Timestamp endDate) {
		Connection connection = ConnectionManager.getConnection();
		ArrayList<DataRecord> list = new ArrayList(1);
		String sql = "SELECT submission_date, indicator_value " +
				" FROM indicators " +
				"WHERE indicator_type_id = ? " +
				"AND submission_date BETWEEN ? AND ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, indicator.getId());
			preparedStatement.setTimestamp(2, startDate);
			preparedStatement.setTimestamp(3, endDate);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					Double data = rs.getDouble("indicator_value");
					Long date = rs.getTimestamp("submission_date").getTime();
					list.add(new DataRecord(data, date, "indicator", indicator.getType()));
				}
				rs.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			preparedStatement.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return list;
	}

	public static ArrayList<Double> getIndicatorValues(Integer id) {
		ArrayList<Double> comfortValues = new ArrayList<>();
		String sql = "SELECT min_comfort_value, max_comfort_value, min_value, max_value " +
				"FROM indicators_type " +
				"WHERE indicators_type.id = ?";
		Connection connection = ConnectionManager.getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, id);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					comfortValues.add(rs.getDouble("min_comfort_value"));
					comfortValues.add(rs.getDouble("max_comfort_value"));
					comfortValues.add(rs.getDouble("min_value"));
					comfortValues.add(rs.getDouble("max_value"));
				}
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return comfortValues;
	}

	public static ArrayList<HashMap> getUserSession(String userId) {
		Connection connection = ConnectionManager.getConnection();
		ArrayList<HashMap> list = new ArrayList(1);
		String sql = "SELECT * FROM sessions WHERE userid=?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, userId);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				list = formatData(rs, 1);
				rs.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			preparedStatement.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return list;
	}

	public static String getUserRole(String email) {
		Connection connection = ConnectionManager.getConnection();
		String sql = "SELECT roles.name " +
				"FROM users " +
				"INNER JOIN roles " +
				"ON roles.id = users.role_id " +
				"WHERE users.email=?";
		String result = null;
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, email);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					result = rs.getString("name");
				}
				rs.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			preparedStatement.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static ArrayList<String> getSettingRole(JSONObject setting){
		ArrayList<String> roles = new ArrayList<String>();
		Connection connection = ConnectionManager.getConnection();
		String sql = "SELECT roles.name " +
				"FROM roles_settings " +
				"INNER JOIN roles " +
				"ON roles.id = roles_settings.role_id " +
				"WHERE roles_settings.setting_id=?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, setting.getInt(setting.getString("setting_id")));
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					roles.add(rs.getString("name"));
				}
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return roles;
	}
	public static JSONArray getUsers() {
		JSONArray users = new JSONArray();
		JSONObject user;
		Connection connection = ConnectionManager.getConnection();
		String sql = "SELECT email,roles.name" +
				" FROM users" +
				"INNER JOIN roles" +
				"ON roles.id = users.role_id";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					String email = rs.getString("email");
					String role = rs.getString("role");
					user = new JSONObject();
					user.put("email", email);
					user.put("role", role);
					users.put(user);
				}
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}
}
