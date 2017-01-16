package sensor.sensorClass;
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

    public ConsumptionSensor(){
        db = new Database();
    }
    public ArrayList<Float> getConsumptiomOnPeriod(Date start, Date end){
        ArrayList<Float> listeOfValue= new ArrayList <Float>();
        listeOfValue= db.getValuePeriod(start,end);
        return listeOfValue;
    }
    public Float getLastConsumption(){
        Float value = db.getLastValue();
        return value;
    }
}
