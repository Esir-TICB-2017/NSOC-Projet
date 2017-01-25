package sensor.sensorClass;

import database.WriteInDatabase;
import sensor.sensorInterface.InterfaceSensors;
import database.Database;

import java.util.ArrayList;
import java.util.Date;



/**
 * Created by mathieu on 16/01/2017.
 */

public class ConsumptionSensor extends Sensor implements InterfaceSensors {


    private static ConsumptionSensor consumptionSensor = new ConsumptionSensor();
    private ConsumptionSensor() { }

    public static ConsumptionSensor getInstance( ) {
        return consumptionSensor;
    }

}
