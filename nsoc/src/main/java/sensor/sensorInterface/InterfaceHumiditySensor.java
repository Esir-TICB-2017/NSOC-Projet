package sensor.sensorInterface;

import java.util.ArrayList;
import java.util.Date;


public interface InterfaceHumiditySensor {
    /**
     * @param start the beginning of the period
     * @param end the end of the period
     * @return a list of data
     **/
    ArrayList<Float> getHumidityOnPeriod(Date start, Date end);


    /**
     * @return the last data
     **/
    float getLastHumidity();

}
