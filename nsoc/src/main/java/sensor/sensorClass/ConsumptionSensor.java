package sensor.sensorClass;
import bacnet.BacNetToJava;
import javafx.scene.chart.PieChart;
import sensor.sensorInterface.InterfaceConsumptionSensor;
import database.Database;

import java.util.ArrayList;
import java.util.Date;



/**
 * Created by mathieu on 16/01/2017.
 */

public class ConsumptionSensor implements InterfaceConsumptionSensor{
    private Database db;
    private double currentValue;

    public ConsumptionSensor(){
        db = new Database();
    }

    public ArrayList<Float> getConsumptiomOnPeriod(Date start, Date end){
        ArrayList<Float> listeOfValue= new ArrayList <Float>();
        //listeOfValue= db.getValuePeriod(start,end);
        return listeOfValue;
    }

    public float getLastConsumption(){
        //float value = db.getLastValue();
        //return value;
        return 1;
    }

//  //ajouter un return pour dire que la bdd a pris la newValue ?
    public void setNewValue(double newValue){
        if (newValue != this.currentValue){

            //appel de la base de données dbSetValue(ConsumptionSensor, this.currentValue)

            // if return(true) ?
            this.currentValue = newValue;
        }


    }

}
