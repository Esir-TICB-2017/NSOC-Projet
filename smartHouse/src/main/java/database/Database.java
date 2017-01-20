package database;
import java.sql.*;


/**
 * Created by loulou on 16/01/2017.
 */
public class Database {

    public static double getValue() {
        Connection connection = ConnectionManager.getConnection();
        Statement statement = null;
        int value = 0;
        try{
            System.out.println("Creating statement...");
            statement = connection.createStatement();
            String className = getClassName();
            String sql = "SELECT * FROM " + className;
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()) {
                //Retrieve by column name
                int id = rs.getInt("id");
                value = rs.getInt("value");
                String date = rs.getString("submission_date");
            }
            rs.close();
            statement.close();
            //connection.close();
            try{
                if(statement!=null) {
                    statement.close();
                }
            } catch(SQLException se2){
            }
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }
        return value;
    }

    public static void writeSensorValue(double value) {
        Connection connection = ConnectionManager.getConnection();
        String className = getClassName();
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
    }

    private static java.sql.Timestamp getCurrentTimeStamp() {

        java.util.Date today = new java.util.Date();
        return new java.sql.Timestamp(today.getTime());

    }

    public static String getClassName() {
        String fullPath = new Exception().getStackTrace()[2].getClassName();
        String[] fullPathArray = fullPath.split("\\.");
        String className = fullPathArray[fullPathArray.length - 1];
        return className;
    }

}