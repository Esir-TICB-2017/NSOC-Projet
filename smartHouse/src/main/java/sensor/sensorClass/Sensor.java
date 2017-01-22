package sensor.sensorClass;

import database.Database;
import database.ReadInDatabase;
import database.WriteInDatabase;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by loulou on 21/01/2017.
 */
public class Sensor {
    public ArrayList<HashMap> getLastValue() {
        return ReadInDatabase.getLastValue(this);
    }

    public ArrayList<HashMap> getValuesOnPeriod(Sensor sensor, Timestamp startDate, Timestamp endDate){
        return  ReadInDatabase.getValuesOnPeriod(this, startDate, endDate);
    }
}
