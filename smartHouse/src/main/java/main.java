import bacnet.BacNetToJava;
import database.Database;
import sensor.sensorClass.ConsumptionSensor;

/**
 * Created by loulou on 18/01/2017.
 */
public class main {
    public static void main (String[] args) {
        BacNetToJava physicalSensor = new BacNetToJava();
        ConsumptionSensor sensor = physicalSensor.getConsumptionSensor();

        while(true) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(sensor.getLastValue());
        }


    }
}
