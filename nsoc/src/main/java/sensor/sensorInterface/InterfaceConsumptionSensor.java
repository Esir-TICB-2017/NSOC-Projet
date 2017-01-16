package sensor.sensorInterface;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mathieu on 16/01/2017.
 */
public interface InterfaceConsumptionSensor {
    /**
     * @return a list of data
     * @param start the beginning of the period
     * @param end the end of the period
     **/
    public ArrayList<Float> getConsumptiomOnPeriod(Date start, Date end);


    /**
     * @return the last data
     **/
    public float getLastConsumption();

}
