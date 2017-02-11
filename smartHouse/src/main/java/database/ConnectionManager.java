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

/**
 * This class allows the program to initiate a connection to the database
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

	/**
	 * get parameters from an external file
	 *
	 * @param parameter
	 * @return
	 */
	public static String getDatabaseParameter(String parameter) {
		String value = null;
		try {
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

	/**
	 * create a JDBConnection
	 * @return
	 */
	private static Connection createConnection() {
		try {
			Class.forName(driverName);
			try {
				connection = DriverManager.getConnection(url, username, password);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return connection;
	}

	/**
	 * This method return a JDBC connection. If the connection is null or closed, opens a new connection
	 *
	 * @return JDBConnection
	 */
	public static Connection getConnection() {
		if (connection == null) {
			connection = createConnection();
		}
		try {
			if (connection.isClosed()) {
				connection = createConnection();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return connection;
	}

	public static void closeConnection() {
		try {
			connection.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}
