package sensor.sensorClass;
import sensor.sensorInterface.InterfaceSensors;
import database.Database;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mathieu on 18/01/2017.
 */
public class HumiditySensor extends Sensor implements InterfaceSensors {

    private static HumiditySensor humiditySensor = new HumiditySensor();
    private HumiditySensor() { }

    public static HumiditySensor getInstance( ) {
        return humiditySensor;
    }
}
