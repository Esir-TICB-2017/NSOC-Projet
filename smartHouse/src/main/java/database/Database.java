package database;
import sensor.sensorClass.Sensor;
import webserver.ConnectedClients;

import javax.json.Json;
import java.sql.*;
import java.util.*;


/**
 * Created by loulou on 16/01/2017.
 */
public class Database {


    public static ArrayList<HashMap> getLastValue(Sensor sensor) {
        Connection connection = ConnectionManager.getConnection();
        String className = sensor.getClass().getSimpleName();
        ArrayList<HashMap> list = new ArrayList(1);
        try (Statement statement = connection.createStatement()){
            String sql = "SELECT * FROM " + className + " ORDER BY submission_date DESC LIMIT 1";
            System.out.println(sql);
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


    public static void writeSensorValue(Sensor sensor, double value) {

        Connection connection = ConnectionManager.getConnection();
        String className = sensor.getClass().getSimpleName();
        String sql = "INSERT INTO " + className
                + "(VALUE, SUBMISSION_DATE) VALUES"
                + "(?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1, value);
            preparedStatement.setTimestamp(2, getCurrentTimeStamp());
            preparedStatement.executeUpdate();


        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        String str = Json.createObjectBuilder().add("value", value).build().toString();
        ConnectedClients.getInstance().writeAllMembers(str);
    }

    private static java.sql.Timestamp getCurrentTimeStamp() {

        java.util.Date today = new java.util.Date();
        return new java.sql.Timestamp(today.getTime());

    }

    private static ArrayList<HashMap> formatData(ResultSet rs, int rows) throws SQLException {
        ArrayList list = new ArrayList(rows);
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();

        while(rs.next()) {
            HashMap row = new HashMap(columns);
            for(int i=1; i<=columns; ++i){
                row.put(md.getColumnName(i),rs.getObject(i));
            }
            list.add(row);
        }
        return list;
    }
}