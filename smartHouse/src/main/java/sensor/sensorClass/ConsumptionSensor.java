package sensor.sensorClass;

import bacnet.BacNetToJava;
import com.sun.tools.classfile.ConstantPool;
import database.ConnectionManager;
import javafx.scene.chart.PieChart;
import sensor.sensorInterface.InterfaceSensors;
import database.Database;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;



/**
 * Created by mathieu on 16/01/2017.
 */

public class ConsumptionSensor implements InterfaceSensors{
    private Database db;
    private double currentValue;

    public ConsumptionSensor(){

    }

    public ArrayList<Float> getValuesOnPeriod(Date start, Date end){
        ArrayList<Float> listeOfValue= new ArrayList <Float>();
        //listeOfValue= db.getValuePeriod(start,end);
        return listeOfValue;
    }

    public double getLastValue() {
        Connection connection = ConnectionManager.getConnection();
        Double value = Database.getValue(connection);
    }

    public void setNewValue(double newValue){
        if (newValue != this.currentValue){

            Connection connection = ConnectionManager.getConnection();
            Database.writeSensorValue(connection, newValue);
            this.currentValue = newValue;
        }


    }

}
