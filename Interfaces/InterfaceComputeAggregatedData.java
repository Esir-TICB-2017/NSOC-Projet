/**
* This class create indicator(s) about house's health
*/

public interface Compute_Indicator {
	/**
	* Create current CO2 indicator
	* Use application's settings using database's interface function (getSettings) to create it
	* @param co2 = Current CO2 sensor's value
	*/
	void createCO2Indicator(float co2);
	/**
	* Create current temparature indicator
	* Use application's settings using database's interface function (getSettings) to create it
	* Call database's interface function (writeHouseIndicator) to write the new value in database
	* @param temparature = Current temparature sensor's value
	*/
	void createTemperatureIndicator(float temperature);
	/**
	* Create current humidity indicator
	* Use application's settings using database's interface function (getSettings) to create it
	* Call database's interface function (writeHouseIndicator) to write the new value in database
	* @param humidity = Current humidity sensor's value
	*/
	void createHumidityIndicator(float humidity);
	/**
	* Create current electricity consumption indicator
	* Use application's settings using database's interface function (getSettings) to create it
	* Call database's interface function (writeHouseIndicator) to write the new value in database
	* @param electricityconsumption = Current house's electricity consumption value
	*/
	void createElectricityConsumptionIndicator(float electricityconsumption);
	/**
	* Create current electricity production indicator
	* Use application's settings using database's interface function (getSettings) to create it
	* Call database's interface function (writeHouseIndicator) to write the new value in database
	* @param electricityproduction = Current house's electricity production value
	*/
	void createElectricityProductionIndicator(float electricityproduction);
	/**
	* Create current electricity ratio indicator
	* Use application's settings using database's interface function (getSettings) to create it
	* Called by createElectricityConsumptionIndicator or/and by createElectricityProductionIndicator
	* Call database's interface function (writeHouseIndicator) to write the new value in database
	*/
	void createElectricityRatioIndicator();
}
