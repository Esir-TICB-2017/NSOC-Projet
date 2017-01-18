package database;
import javafx.scene.chart.PieChart;

import java.sql.*;


/**
 * Created by loulou on 16/01/2017.
 */
public class Database {

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "izi";

    public static double getValue(Connection connection) {
        Statement statement = null;
        int value = 0;
        try{
            System.out.println("Creating statement...");
            statement = connection.createStatement();
            String sql;
            sql = "SELECT * FROM consumption";
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()) {
                //Retrieve by column name
                int id = rs.getInt("id");
                value = rs.getInt("value");
                String date = rs.getString("submission_date");
            }
            rs.close();
            statement.close();
            connection.close();
            try{
                if(statement!=null) {
                    statement.close();
                }
            } catch(SQLException se2){
            }// nothing we can do
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }
        return value;
    }

    public static void writeSensorValue(Connection connection, double value) {
        String sql = "INSERT INTO consumption"
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
    }

    private static java.sql.Timestamp getCurrentTimeStamp() {

        java.util.Date today = new java.util.Date();
        return new java.sql.Timestamp(today.getTime());

    }

}