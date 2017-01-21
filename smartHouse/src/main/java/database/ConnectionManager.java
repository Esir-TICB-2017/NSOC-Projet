package database;


import java.io.File;
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
    private static String domain = getDatabaseParameters("domain");
    private static String port = getDatabaseParameters("port");
    private static String databaseName = getDatabaseParameters("databaseName");
    private static String username = getDatabaseParameters("username");
    private static String password = getDatabaseParameters("password");
    private static Connection connection;
    private static String driverName = "com.mysql.jdbc.Driver";
    private static String url = "jdbc:mysql://" + domain + ":" + port + "/" + databaseName;

    public static String getDatabaseParameters(String parameter) {
        String value = null;
        try{
            String filePath = new File("").getAbsolutePath();
            FileReader file = new FileReader(filePath + "/src/main/java/database/test.json");
            JSONTokener tokener = new JSONTokener(file);
            JSONObject obj = new JSONObject(tokener);
            value = obj.getJSONObject("info").getString(parameter);
        } catch (FileNotFoundException f) {
            f.printStackTrace();
        }
        return value;
    }

    private static Connection createConnection() {
        try {
            Class.forName(driverName);
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                // log an exception. for example:
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            // log an exception. for example:
            e.printStackTrace();
        }
        return connection;
    }

    public static Connection getConnection() {
        if(connection == null){
            connection = createConnection();
        }
        try {
            if(connection.isClosed()){
                connection = createConnection();
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection(){
        try{
            connection.close();
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
}
