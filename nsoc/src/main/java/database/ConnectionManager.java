package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by loulou on 16/01/2017.
 */
public class ConnectionManager {
    private static String url = "jdbc:mysql://localhost/nsoc_database";
    private static String driverName = "com.mysql.jdbc.Driver";
    private static String username = "root";
    private static String password = "izi";
    private static Connection connection;
    private static String urlstring;

    public static Connection getConnection() {
        try {
            Class.forName(driverName);
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException ex) {
                // log an exception. for example:
                System.out.println("Failed to create the database connection.");
            }
        } catch (ClassNotFoundException ex) {
            // log an exception. for example:
            System.out.println("Driver not found.");
        }
        return connection;
    }


}
