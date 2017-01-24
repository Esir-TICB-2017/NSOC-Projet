package database;

import database.databaseInterface.InterfaceWriteDatabase;
import sensor.sensorClass.Sensor;
import webserver.ConnectedClients;

import javax.json.Json;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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






}
