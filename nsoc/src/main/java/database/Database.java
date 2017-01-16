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
        try{
            System.out.println("Creating statement...");
            statement = connection.createStatement();
            String sql;
            sql = "SELECT * FROM temperature";
            ResultSet rs = statement.executeQuery(sql);

            while(rs.next()){
                //Retrieve by column name
                int id  = rs.getInt("id");
                int value = rs.getInt("value");
                String date = rs.getString("submission_date");

                //Display value
                System.out.print("ID: " + id);
                System.out.print(", value: " + value);
                System.out.println(", date: " + date);

                rs.close();
                statement.close();
                return value;
            }
            try{
                if(statement!=null)
                    statement.close();
            }catch(SQLException se2){
            }// nothing we can do
        }catch(Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static void main (String[] args) {
        Connection connection = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(DB_URL,USER,PASS);

            getValue(connection);

            connection.close();

        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resource
            try{
                if(connection!=null)
                    connection.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
    }// end main

}

