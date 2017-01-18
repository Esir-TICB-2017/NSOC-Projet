package sensor.sensorClass;
import sensor.sensorInterface.InterfaceSensors;
import database.Database;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mathieu on 18/01/2017.
 */
public class ProductionSensor implements InterfaceSensors{

    private Database db;
    private double currentValue;

    public ProductionSensor(){
        db = new Database();
    }

    public ArrayList<Float> getValuesOnPeriod(Date start, Date end){
        ArrayList<Float> listeOfValue= new ArrayList <Float>();
        //listeOfValue= db.getValuePeriod(start,end);
        return listeOfValue;
    }

    public float getLastValue(){
        //float value = db.getLastValue();
        int value=1; // suppsr
        return value;
    }

    public void setNewValue(double newValue){
        if (newValue != this.currentValue){

            //AJOUTER l'argument du type de sensor
            //appel de la base de données
            //      db.writeValue(this.currentValue);

            //AJOUTER un if(return==true) pour confirmer que la bdd a pris la newValue?
            this.currentValue = newValue;
            //APPERLER la méthode createIndicators()
        }


    }
}
