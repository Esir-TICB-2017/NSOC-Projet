package sensor.sensorClass;
import sensor.sensorInterface.InterfaceConsumptionSensor;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by mathieu on 16/01/2017.
 */

public class ConsumptionSensor implements InterfaceConsumptionSensor{
    public ArrayList<Float> getConsumptiomOnPeriod(Date start, Date end){
        ArrayList<Float> liste= new ArrayList <Float>(10);

        return liste;
    }
    public float getLastConsumption(){

        return 1;
    }
}
