package database;

import computeAggregatedData.Indicators;
import database.data.DataLinkToDate;
import database.databaseInterface.InterfaceReadDatabase;
import sensor.sensorClass.Sensor;
import sensor.sensorClass.Sensors;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

	public static ArrayList<String> getAllSensorsName() {
		Connection connection = ConnectionManager.getConnection();
		ArrayList<String> SensorsNamelist = new ArrayList(1);
		String sql = "SELECT type_name FROM sensor_type";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					String name = rs.getString("type_name");
					SensorsNamelist.add(name);
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return SensorsNamelist;
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


	public static ArrayList<DataLinkToDate> getAllLastIndicators() {
		ArrayList<DataLinkToDate> list = new ArrayList(Indicators.ALL_INDICATORS.length);

		for (String indicator : Indicators.ALL_INDICATORS) {
			list.add(getLastIndicator(indicator));
		}
		return list;
	}

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

	public static DataLinkToDate getLastIndicator(String indicator) {
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
			preparedStatement.setString(1, indicator);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					date = rs.getTimestamp("submission_date");
					data = rs.getDouble("indicator_value");
					result = new DataLinkToDate(data, date, "indicator", indicator);
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static ArrayList<DataLinkToDate> getIndicatorsOnPeriod(String indicator, Timestamp startDate, Timestamp endDate) {
		double data;
		Timestamp date;
		Connection connection = ConnectionManager.getConnection();
		ArrayList<DataLinkToDate> list = new ArrayList(1);
		String sql = "SELECT indicators.submission_date, indicators.indicator_value" +
				" FROM indicators " +
				"INNER JOIN indicators_type " +
				"ON indicators.indicator_type_id = indicators_type.id " +
				"WHERE indicators_type.type_name = ? " +
				"AND indicators.submission_date BETWEEN ? AND ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, indicator);
			preparedStatement.setTimestamp(2, startDate);
			preparedStatement.setTimestamp(3, endDate);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					date = rs.getTimestamp("submission_date");
					data = rs.getDouble("indicator_value");
					list.add(new DataLinkToDate(data, date, "indicator", indicator));
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return list;
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
}
