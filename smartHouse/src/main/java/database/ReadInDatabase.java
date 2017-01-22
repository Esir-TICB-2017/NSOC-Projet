package database;

import database.databaseInterface.InterfaceReadDatabase;
import sensor.sensorClass.Sensor;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by loulou on 21/01/2017.
 */
public class ReadInDatabase extends Database implements InterfaceReadDatabase {
    public static ArrayList<HashMap> getLastValue(Sensor sensor) {
        Connection connection = ConnectionManager.getConnection();
        String className = sensor.getClass().getSimpleName();
        ArrayList<HashMap> list = new ArrayList(1);
        try (Statement statement = connection.createStatement()){
            String sql = "SELECT * FROM " + className + " ORDER BY submission_date DESC LIMIT 1";
            try(ResultSet rs = statement.executeQuery(sql)) {
                list = formatData(rs, 1);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public static ArrayList<HashMap> getValuesOnPeriod(Sensor sensor, Timestamp startDate, Timestamp endDate) {
        Connection connection = ConnectionManager.getConnection();
        String className = sensor.getClass().getSimpleName();
        ArrayList<HashMap> list = new ArrayList(1);
        String sql = "SELECT * FROM " + className +
                " WHERE submission_date BETWEEN ? AND ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setTimestamp(1, startDate);
            preparedStatement.setTimestamp(2, endDate);
            try(ResultSet rs = preparedStatement.executeQuery()) {
                list = formatData(rs, 1);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public static ArrayList<HashMap> getUser(String userId) throws Exception{
        Connection connection = ConnectionManager.getConnection();
        ArrayList<HashMap> list = new ArrayList(1);
        String sql = "SELECT * FROM users WHERE userid=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, userId);
            try(ResultSet rs = preparedStatement.executeQuery()) {
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
