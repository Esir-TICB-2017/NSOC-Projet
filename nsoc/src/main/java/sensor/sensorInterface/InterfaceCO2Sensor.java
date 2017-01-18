package sensor.sensorInterface;

import java.util.ArrayList;
import java.util.Date;


public interface InterfaceCO2Sensor {
    /**
     * @param start the beginning of the period
     * @param end the end of the period
     * @return a list of data
     **/
    ArrayList<Float> getCO2OnPeriod(Date start, Date end);


    /**
     * @return the last data
     **/
    float getLastCO2();

}
