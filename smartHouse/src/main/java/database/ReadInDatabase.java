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
import java.util.StringJoiner;

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
