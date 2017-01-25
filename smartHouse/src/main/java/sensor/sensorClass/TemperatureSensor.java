package sensor.sensorClass;
import sensor.sensorInterface.InterfaceSensors;
import database.Database;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mathieu on 18/01/2017.
 */
public class TemperatureSensor extends Sensor implements InterfaceSensors {

    private static TemperatureSensor temperatureSensor = new TemperatureSensor();
    private TemperatureSensor() { }

    public static TemperatureSensor getInstance( ) {
        return temperatureSensor;
    }
}
