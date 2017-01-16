package database;
import java.sql.*;


/**
 * Created by loulou on 16/01/2017.
 */
public class Database {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/nsoc_database";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "izi";

    public static int getValue(Connection connection) {
        Statement statement = null;
        int value = 0;
        try{
            System.out.println("Creating statement...");
            statement = connection.createStatement();
            String sql;
            sql = "SELECT * FROM temperature";
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

    public static void main (String[] args) {
        Connection connexion = ConnectionManager.getConnection();
        int value = getValue(connexion);
        System.out.println(value);


    }

}

