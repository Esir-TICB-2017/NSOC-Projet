package sensor.sensorClass;
import sensor.sensorInterface.InterfaceSensors;
import database.Database;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mathieu on 18/01/2017.
 */
public class CO2Sensor extends Sensor implements InterfaceSensors {
    private static CO2Sensor co2Sensor = new CO2Sensor();
    private CO2Sensor() { }

    public static CO2Sensor getInstance( ) {
        return co2Sensor;
    }
}
