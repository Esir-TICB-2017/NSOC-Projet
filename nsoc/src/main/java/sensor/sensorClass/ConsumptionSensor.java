package sensor.sensorClass;
import sensor.sensorInterface.InterfaceConsumptionSensor;
import database.Database;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by mathieu on 16/01/2017.
 */

public class ConsumptionSensor implements InterfaceConsumptionSensor{
    public ArrayList<Float> getConsumptiomOnPeriod(Date start, Date end){
        ArrayList<Float> listeOfValue= new ArrayList <Float>();
        listeOfValue= getValuePeriod(start,end);
        return listeOfValue;
    }
    public Float getLastConsumption(){
        Float value = getLastValue();
        return value;
    }
}
