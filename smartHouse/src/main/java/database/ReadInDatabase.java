package database;

import computeAggregatedData.Indicators;
import database.data.DataLinkToDate;
import database.databaseInterface.InterfaceReadDatabase;
import jdk.nashorn.api.scripting.JSObject;
import org.json.JSONObject;
import sensor.sensorClass.Sensor;
import sensor.sensorClass.Sensors;

import javax.xml.crypto.Data;
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
        DataLinkToDate  dltd = null;
        Connection connection = ConnectionManager.getConnection();
        String className = sensor.getClass().getSimpleName();
        ArrayList<DataLinkToDate> list = new ArrayList(1);
        try (Statement statement = connection.createStatement()){
            String sql = "SELECT submission_date,value FROM " + className + " ORDER BY submission_date DESC LIMIT 1";
            try(ResultSet rs = statement.executeQuery(sql)) {
                while(rs.next()){
                    date = rs.getTimestamp("submission_date");
                    data = rs.getDouble("value");
                    dltd = new DataLinkToDate(data, date, "sensor", className);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return dltd;
    }

    public static ArrayList<DataLinkToDate> getValuesOnPeriod(Sensor sensor, Timestamp startDate, Timestamp endDate) {
        double data;
        Timestamp date;
        Connection connection = ConnectionManager.getConnection();
        String className = sensor.getClass().getSimpleName();
        ArrayList<DataLinkToDate> list = new ArrayList(1);
        String sql = "SELECT submission_date,value FROM " + className +
                " WHERE submission_date BETWEEN ? AND ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setTimestamp(1, startDate);
            preparedStatement.setTimestamp(2, endDate);
            try(ResultSet rs = preparedStatement.executeQuery()) {
                while(rs.next()){
                    date = rs.getTimestamp("submission_date");
                    data = rs.getDouble("value");
                    list.add(new DataLinkToDate(data,date, "sensor", className));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public static ArrayList<DataLinkToDate> getAllLastIndicators(){
        ArrayList<DataLinkToDate> list = new ArrayList(Indicators.ALL_INDICATORS.length);

        for(String indicator : Indicators.ALL_INDICATORS) {
            list.add(getLastIndicator(indicator));
        }
        return list;
    }

    public static ArrayList<DataLinkToDate> getAllLastValues(){
        List<Sensor> sensors = Sensors.getInstance().getSensors();
        ArrayList<DataLinkToDate> list = new ArrayList(sensors.size());

        for(Sensor sensor : sensors) {
            list.add(getLastValue(sensor));
        }
        return list;
    }

    public static DataLinkToDate getLastIndicator(String indicator){
        DataLinkToDate indicateur = null;
        double data;
        Timestamp date;
        Connection connection = ConnectionManager.getConnection();
        String sql = "SELECT submission_date,value FROM " + indicator + " ORDER BY submission_date DESC LIMIT 1";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            try(ResultSet rs = preparedStatement.executeQuery(sql)) {
                while(rs.next()){
                    date = rs.getTimestamp("submission_date");
                    data = rs.getDouble("value");
                    indicateur = new DataLinkToDate(data,date, "indicator", indicator);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return indicateur;
    }

    public static ArrayList<DataLinkToDate> getIndicatorsOnPeriod(String indicator, Timestamp startDate, Timestamp endDate) {
        double data;
        Timestamp date;
        Connection connection = ConnectionManager.getConnection();
        ArrayList<DataLinkToDate> list = new ArrayList(1);
        String sql = "SELECT submission_date,value FROM " + indicator +
                " WHERE submission_date BETWEEN ? AND ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setTimestamp(1, startDate);
            preparedStatement.setTimestamp(2, endDate);
            try(ResultSet rs = preparedStatement.executeQuery()) {
                while(rs.next()){
                    date = rs.getTimestamp("submission_date");
                    data = rs.getDouble("value");
                    list.add(new DataLinkToDate(data,date, "indicator", indicator));
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

    public static ArrayList<HashMap> getUserSession(String userId) {
        Connection connection = ConnectionManager.getConnection();
        ArrayList<HashMap> list = new ArrayList(1);
        String sql = "SELECT * FROM sessions WHERE userid=?";
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
