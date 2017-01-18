package sensor.sensorInterface;

import java.util.ArrayList;
import java.util.Date;

/***
 * Created by mathieu on 16/01/2017.
 */
public interface InterfaceSensors {
    /**
     * @param start the beginning of the period
     * @param end the end of the period
     * @return a list of data
     **/
    ArrayList<Float> getValuesOnPeriod(Date start, Date end);


    /**
     * @return the last data
     **/
    double getLastValue();


    /**
     * Write new value in database
     * Update current value
     * Call method to create indicators
     * @param newValue new value from sensor
     **/
    void setNewValue(double newValue);


}
