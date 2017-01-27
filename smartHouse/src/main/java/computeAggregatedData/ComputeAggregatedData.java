package computeAggregatedData;

import database.WriteInDatabase;
import database.data.DataLinkToDate;
import sensor.sensorClass.*;
import database.Database;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mathieu on 20/01/2017.
 */
public class ComputeAggregatedData implements InterfaceComputeAggregatedData{
    private Database db;
    private ConsumptionSensor consumption;
    private ProductionSensor production;
    private CO2Sensor co2;
    private HumiditySensor humidity;
    private TemperatureSensor temperature;
    private int co2Indicator;
    private int humidityIndicator;
    private int temperatureIndicator;
    private int productionIndicator;
    private int consumptionIndicator;

    public ComputeAggregatedData(){
        db = new Database();
        consumption = ConsumptionSensor.getInstance();
        production =  ProductionSensor.getInstance();
        co2 = CO2Sensor.getInstance();
        humidity = HumiditySensor.getInstance();
        temperature = TemperatureSensor.getInstance();

    }

// Il manque les indicateurs de consommation, de production et de temparture
    public void createGlobalIndicator() {
        int globalIndicator = (int)(co2Indicator+humidityIndicator+temperatureIndicator+productionIndicator+consumptionIndicator)/5;
        WriteInDatabase.writeIndicatorValue(Indicators.GLOBAL, globalIndicator);

    }


    public void  createCO2Indicator() {
        double currentCO2Value = co2.getLastValue().getData();
         co2Indicator = 100;
        if(currentCO2Value <= 600){
            // do nothing
        }
        else if(currentCO2Value <= 800){
            co2Indicator = co2Indicator - (int)((currentCO2Value - 600)/20);
        }
        else{
            co2Indicator = 90;
            co2Indicator = co2Indicator - (int)((currentCO2Value - 800)/10);
            if(co2Indicator < 0) { co2Indicator = 0; }
        }
        WriteInDatabase.writeIndicatorValue(Indicators.CO2, co2Indicator);
    }


    public void createTemperatureIndicator() {
// il faut lire en base le settings
        double temperatureSetpoint = 20 ;
        double currentTemperatureValue = temperature.getLastValue().getData();
        temperatureIndicator = 100;
        if(currentTemperatureValue > temperatureSetpoint){
            temperatureIndicator = temperatureIndicator - (int)((currentTemperatureValue - temperatureSetpoint)*20);
            if (temperatureIndicator < 0){ temperatureIndicator = 0;}
        }
        else if (currentTemperatureValue < temperatureSetpoint){
            temperatureIndicator = temperatureIndicator - (int)((temperatureSetpoint - currentTemperatureValue)*20);
            if (temperatureIndicator < 0){ temperatureIndicator = 0;}
        }

        WriteInDatabase.writeIndicatorValue(Indicators.TEMPERATURE, temperatureIndicator);
    }


    public void createHumidityIndicator() {
        double currentHumidityValue = humidity.getLastValue().getData();
        humidityIndicator = 100;
        if(currentHumidityValue < 50){
            humidityIndicator = 100 - (int)((50 - currentHumidityValue)*2);
        }
        else{
            humidityIndicator = 100 - (int)((currentHumidityValue - 50)*2);
        }
        WriteInDatabase.writeIndicatorValue(Indicators.HUMIDITY, humidityIndicator);
    }


    public void createElectricityConsumptionIndicator() {
// il faut lire en base le settings
        double consumptionSetpoint = 20 ;
        double currentConsumptionValue = consumption.getLastValue().getData();
        consumptionIndicator = 100;
        if(currentConsumptionValue > consumptionSetpoint){
            consumptionIndicator = consumptionIndicator - (int)((currentConsumptionValue - consumptionSetpoint));
        }
        WriteInDatabase.writeIndicatorValue(Indicators.CONSUMPTION, consumptionIndicator);
    }


    public void createElectricityProductionIndicator() {
// il faut lire en base le settings
        double productionSetpoint = 20;
        double currentProductionValue = production.getLastValue().getData();
        productionIndicator = 100;
        if(currentProductionValue < productionSetpoint){
            productionIndicator = productionIndicator - (int)((currentProductionValue - productionSetpoint));
        }
        WriteInDatabase.writeIndicatorValue(Indicators.PRODUCTION, productionIndicator);
    }

}
