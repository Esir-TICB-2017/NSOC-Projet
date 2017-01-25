package sensor.sensorClass;
import sensor.sensorInterface.InterfaceSensors;
import database.Database;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mathieu on 18/01/2017.
 */
public class ProductionSensor extends Sensor implements InterfaceSensors{

    private static ProductionSensor productionSensor = new ProductionSensor();
    private ProductionSensor() { }

    public static ProductionSensor getInstance( ) {
        return productionSensor;
    }
}
