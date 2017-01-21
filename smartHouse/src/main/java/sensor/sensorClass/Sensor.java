package sensor.sensorClass;

import database.Database;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by loulou on 21/01/2017.
 */
public class Sensor {
    public ArrayList<HashMap> getLastValue() {
        return Database.getLastValue(this);
    }

    public ArrayList<HashMap> getValuesOnPeriod(Sensor sensor, Timestamp startDate, Timestamp endDate){
        return  Database.getValuesOnPeriod(this, startDate, endDate);
    }
}
