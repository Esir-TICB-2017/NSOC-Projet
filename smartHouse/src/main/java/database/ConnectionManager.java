package database;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.json.*;

/**
 * Created by loulou on 16/01/2017.
 */
public class ConnectionManager {
    private static String domain = getDatabaseParameter("domain");
    private static String port = getDatabaseParameter("port");
    private static String databaseName = getDatabaseParameter("databaseName");
    private static String username = getDatabaseParameter("username");
    private static String password = getDatabaseParameter("password");
    private static Connection connection;
    private static String driverName = "com.mysql.jdbc.Driver";
    private static String url = "jdbc:mysql://" + domain + ":" + port + "/" + databaseName;

    public static String getDatabaseParameter(String parameter) {
        String value = null;
        try{

            FileReader file = new FileReader("/Users/loulou/projets_2016/NSOC-Projet/smartHouse/src/main/java/database/test.json");
            JSONTokener tokener = new JSONTokener(file);
            JSONObject obj = new JSONObject(tokener);
            value = obj.getJSONObject("info").getString(parameter);
        } catch (FileNotFoundException f) {
            f.printStackTrace();
        }
        return value;
    }

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
