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
        DatabaseEventsHandler.broadcastSensorValue(sensor, value);

    }

    public static void deleteUserSession(String userId) {

        Connection connection = ConnectionManager.getConnection();
        String sql = "DELETE FROM sessions WHERE userid=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userId);
            preparedStatement.executeUpdate();
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void storeNewSession(String token, String userId, Timestamp expirationDate){
        Connection connection = ConnectionManager.getConnection();
        String sql = "INSERT INTO sessions (userid, token, expiration_date) VALUES (?, ?, ?)";
        String updateSql ="UPDATE sessions SET userid = ? , token = ?, expiration_date = ? WHERE userid = ?";
        try  {
            ArrayList<HashMap> userInfo = ReadInDatabase.getUserSession(userId);
            if(!userInfo.isEmpty()) {
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(updateSql);
                    preparedStatement.setString(1, userId);
                    preparedStatement.setString(2, token);
                    preparedStatement.setTimestamp(3, expirationDate);
                    preparedStatement.setString(4, userId);
                    preparedStatement.executeUpdate();
                } catch(SQLException e){
                    e.printStackTrace();
                }
            } else {
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, userId);
                    preparedStatement.setString(2, token);
                    preparedStatement.setTimestamp(3, expirationDate);
                    preparedStatement.executeUpdate();
                } catch(SQLException e){
                   e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }






}
