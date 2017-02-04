package database;
import sensor.sensorClass.Sensor;

import java.sql.*;
import java.util.*;


/**
 * Created by loulou on 16/01/2017.
 */
public class Database {

    protected static java.sql.Timestamp getCurrentTimeStamp() {
        java.util.Date today = new java.util.Date();
        return new java.sql.Timestamp(today.getTime());
    }

    protected static ArrayList<HashMap> formatData(ResultSet rs, int rows) throws SQLException {
        ArrayList list = new ArrayList(rows);
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();

        while(rs.next()) {
            HashMap row = new HashMap(columns);
            for(int i=1; i<=columns; ++i){
                    row.put(md.getColumnName(i),rs.getObject(i));
            }
            list.add(row);
        }
        return list;
    }
}