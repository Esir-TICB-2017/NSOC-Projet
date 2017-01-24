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
    private double currentValue;

    public ConsumptionSensor(){
    }

    public void setNewValue(double newValue){
        if (newValue != this.currentValue){
            WriteInDatabase.writeSensorValue(this, newValue);
            this.currentValue = newValue;
        }


    }

}
