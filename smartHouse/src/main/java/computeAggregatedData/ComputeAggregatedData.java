package computeAggregatedData;

import sensor.sensorClass.*;
import database.Database;
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

    public ComputeAggregatedData(){
        db = new Database();
        consumption = new ConsumptionSensor();
        production =  new ProductionSensor();
        co2 = new CO2Sensor();
        humidity = new HumiditySensor();
        temperature = new TemperatureSensor();
    }
    public void createGlobalIndicator() {
        Double currentConsumptionValue = consumption.getLastValue();
        Double currentProductionValue = production.getLastValue();
        Double currentCO2Value = co2.getLastValue();
        Double currentHumidityValue = humidity.getLastValue();
        Double currentTemperatureValue = temperature.getLastValue();
        /*
        Double consumptionSetpoint = db.consumptionSetpoint();
        Double productionSetpoint = db.productionSetpoint();
        Double co2Setpoint = db.co2Setpoint();
        Double humiditySetpoint = db.humiditySetpoint();
        Double temperatureSetpoint = db.temperatureSetpoint();
        */
        //ici mettre notre méga formule pour calculer l'indicateur

        //écrire cet indicateur en base et l'envoyer a l'interface si il est différent du dernier
    }

    public void createCO2Indicator(float currentCO2Value) {
        //Double co2Setpoint = db.co2Setpoint();
        //Double indicator = co2Setpoint - currentCO2Value;
        //mettre en base
    }
    public void createTemperatureIndicator(float currentTemperature) {
        //Double temperatureSetpoint = db.temperatureSetpoint();
        //Double indicator = temperatureSetpoint - currentTemperature;
        //mettre en base
    }
    public void createHumidityIndicator(float currentHumidity) {
        //Double humiditySetpoint = db.humiditySetpoint();
        //Double indicator = humiditySetpoint - currentHumidity;
        //mettre en base
    }
    public void createElectricityConsumptionIndicator(float currentElectricityConsumption) {
        //Double consumptionSetpoint = db.consumptionSetpoint();
        //Double indicator = consumptionSetpoint - currentElectricityConsumption;
        //mettre en base
    }
    public void createElectricityProductionIndicator(float currentElectricityProduction) {
        //Double productionSetpoint = db.productionSetpoint();
        //Double indicator = productionSetpoint - currentElectricityProduction;
        //mettre en base
    }
    public void createElectricityRatioIndicator() {
        Double currentConsumptionValue = consumption.getLastValue();
        Double currentProductionValue = production.getLastValue();
        Double indicator = currentProductionValue- currentConsumptionValue;
        //traiter l'indicateur
        //mettre l'indicateur en base
    }
}
