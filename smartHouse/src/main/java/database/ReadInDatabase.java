package database;

import database.data.DataLinkToDate;
import database.databaseInterface.InterfaceReadDatabase;
import indicators.Indicator;
import sensor.sensorClass.Sensor;
import sensor.sensorClass.Sensors;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;
import java.util.Map;

/**
 * Created by loulou on 21/01/2017.
 */
public class ReadInDatabase extends Database implements InterfaceReadDatabase {


	public static DataLinkToDate getLastValue(Sensor sensor) {
		double data;
		Timestamp date;
		DataLinkToDate result = null;
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
					date = rs.getTimestamp("submission_date");
					data = rs.getDouble("sensor_value");
					result = new DataLinkToDate(data, date, "sensor", sensorType);
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static Map<String, Integer> getAllSensorsName() {
		Connection connection = ConnectionManager.getConnection();
		Map<String, Integer> Sensorslist = new HashMap<String, Integer>(1);
		String sql = "SELECT type_name, id FROM sensor_type";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					String name = rs.getString("type_name");
					Integer id = rs.getInt("id");
					Sensorslist.put(name, id);

				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return Sensorslist;
	}
	public static Map<String, Integer> getAllIndicatorsName() {
		Connection connection = ConnectionManager.getConnection();
		Map<String, Integer> Indicatorslist = new HashMap<String, Integer>(1);
		String sql = "SELECT type_name, id FROM indicators_type";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					String name = rs.getString("type_name");
					Integer id = rs.getInt("id");
					Indicatorslist.put(name, id);

				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return Indicatorslist;
	}


	public static ArrayList<DataLinkToDate> getValuesOnPeriod(Sensor sensor, Timestamp startDate, Timestamp endDate) {
		double data;
		Timestamp date;
		Connection connection = ConnectionManager.getConnection();
		String sensorType = sensor.getType();
		ArrayList<DataLinkToDate> list = new ArrayList(1);
		String sql = "SELECT sensors_data.submission_date, sensors_data.sensor_value, sensor_type.type_name " +
				"FROM sensors_data INNER JOIN sensor_type " +
				"ON sensors_data.sensor_type_id=sensor_type.id " +
				"AND sensor_type.type_name = ? " +
				" WHERE sensors_data.submission_date BETWEEN ? AND ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, sensorType);
			preparedStatement.setTimestamp(2, startDate);
			preparedStatement.setTimestamp(3, endDate);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					date = rs.getTimestamp("submission_date");
					data = rs.getDouble("value");
					list.add(new DataLinkToDate(data, date, "sensor", sensorType));
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return list;
	}

/*
	public static ArrayList<DataLinkToDate> getAllLastIndicators() {

		ArrayList<DataLinkToDate> list = new ArrayList(Indicators.ALL_INDICATORS.length);

		for (String indicator : Indicators.ALL_INDICATORS) {
			list.add(getLastIndicator(indicator));
		}
		return list;

	}
	*/

	public static ArrayList<DataLinkToDate> getLastSensorsValues() {
		ArrayList<DataLinkToDate> list = new ArrayList();
		Connection connection = ConnectionManager.getConnection();
		String sql = "SELECT sensors_data.submission_date, sensors_data.sensor_value, sensor_type.type_name " +
				"FROM sensors_data LEFT JOIN sensor_type " +
				"ON sensors_data.sensor_type_id=sensor_type.id " +
				"WHERE sensors_data.submission_date = (SELECT MAX(sensors_data.submission_date) FROM sensors_data WHERE sensors_data.sensor_type_id=sensor_type.id) " +
				"ORDER BY sensor_type.type_name";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					Timestamp date = rs.getTimestamp("submission_date");
					Double data = rs.getDouble("sensor_value");
					String type = rs.getString("type_name");
					DataLinkToDate row = new DataLinkToDate(data, date, "sensor", type);
					list.add(row);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	public static ArrayList<DataLinkToDate> getLastIndicatorsValues() {
		ArrayList<DataLinkToDate> list = new ArrayList();
		Connection connection = ConnectionManager.getConnection();
		String sql = "SELECT indicators.submission_date, indicators.sensor_value, indicators_type.type_name " +
				"FROM indicators LEFT JOIN indicators_type " +
				"ON indicators.indicator_type_id=indicators_type.id " +
				"WHERE indicators.submission_date = (SELECT MAX(indicators.submission_date) FROM indicators WHERE indicators.indicators_type_id=indicators_type.id) " +
				"ORDER BY indicators_type.type_name";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					Timestamp date = rs.getTimestamp("submission_date");
					Double data = rs.getDouble("indicator_value");
					String type = rs.getString("type_name");
					DataLinkToDate row = new DataLinkToDate(data, date, "indicator", type);
					list.add(row);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static DataLinkToDate getLastIndicator(Indicator indicator) {
		DataLinkToDate result = null;
		double data;
		Timestamp date;
		Connection connection = ConnectionManager.getConnection();
		String sql = "SELECT indicators.submission_date, indicators.indicator_value " +
				"FROM indicators " +
				"INNER JOIN indicators_type " +
				"ON indicators.indicator_type_id = indicators_type.id " +
				"WHERE indicators_type.type_name = ? " +
				"ORDER BY indicators.submission_date DESC LIMIT 1";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			System.out.println(indicator.getId());
			preparedStatement.setString(1, indicator.getType());
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					date = rs.getTimestamp("submission_date");
					data = rs.getDouble("indicator_value");
					result = new DataLinkToDate(data, date, "indicator", indicator.getType());
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static ArrayList<DataLinkToDate> getIndicatorsOnPeriod(Indicator indicator, Timestamp startDate, Timestamp endDate) {
		double data;
		Timestamp date;
		String type = indicator.getType();
		Connection connection = ConnectionManager.getConnection();
		ArrayList<DataLinkToDate> list = new ArrayList(1);
		String sql = "SELECT indicators.submission_date, indicators.indicator_value " +
				" FROM indicators " +
				"INNER JOIN indicators_type " +
				"ON indicators.indicator_type_id = indicators_type.id " +
				"WHERE indicators_type.type_name = ? " +
				"AND indicators.submission_date BETWEEN ? AND ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, type);
			preparedStatement.setTimestamp(2, startDate);
			preparedStatement.setTimestamp(3, endDate);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					date = rs.getTimestamp("submission_date");
					data = rs.getDouble("indicator_value");
					list.add(new DataLinkToDate(data, date, "indicator", type));
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
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
		try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, id);
			try(ResultSet rs = preparedStatement.executeQuery()) {
				while(rs.next()) {
					comfortValues.add(rs.getDouble("min_comfort_value"));
					comfortValues.add(rs.getDouble("max_comfort_value"));
					comfortValues.add(rs.getDouble("min_value"));
					comfortValues.add(rs.getDouble("max_value"));
				}
			}catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return comfortValues;
	}

	public static ArrayList<HashMap> getUser(String userId) {
		Connection connection = ConnectionManager.getConnection();
		ArrayList<HashMap> list = new ArrayList(1);
		String sql = "SELECT * FROM users WHERE userid=?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, userId);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				list = formatData(rs, 1);
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return list;
	}

	public static ArrayList<HashMap> getUserSession(String userId) {
		Connection connection = ConnectionManager.getConnection();
		ArrayList<HashMap> list = new ArrayList(1);
		String sql = "SELECT * FROM sessions WHERE userid=?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, userId);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				list = formatData(rs, 1);
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return list;
	}

	public static ArrayList<String> getSettings(String userId) {

		Connection connection = ConnectionManager.getConnection();
		ArrayList<String> listSettings = new ArrayList();
		String sql = "SELECT * FROM settings WHERE userid=?";

		//Values attribution and query execution
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, userId);
			try(ResultSet rs = preparedStatement.executeQuery()) {
				listSettings.set(0, rs.getString("settemp"));
				listSettings.set(1, rs.getString("setco2min"));
				listSettings.set(2, rs.getString("setco2max"));
				listSettings.set(3, rs.getString("setconsobj"));
				listSettings.set(4, rs.getString("setprodobj"));

				listSettings.set(5, rs.getString("tempind"));
				listSettings.set(6, rs.getString("humidityind"));
				listSettings.set(7, rs.getString("co2ind"));
				listSettings.set(8, rs.getString("consind"));
				listSettings.set(9, rs.getString("prodind"));
				listSettings.set(10, rs.getString("perhome"));

				listSettings.set(11, rs.getString("globalchart"));
				listSettings.set(12, rs.getString("tempchart"));
				listSettings.set(13, rs.getString("humiditychart"));
				listSettings.set(14, rs.getString("co2chart"));
				listSettings.set(14, rs.getString("conschart"));
				listSettings.set(16, rs.getString("prodchart"));
				listSettings.set(17, rs.getString("perdata"));
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listSettings;
	}
}
