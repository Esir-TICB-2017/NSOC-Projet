package computeAggregatedData;

/**
* This class create indicator(s) about house's health
*/

public interface InterfaceComputeAggregatedData {
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
	* @param temperature = Current temperature sensor's value
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
	* @param electricityConsumption = Current house's electricity consumption value
	*/
	void createElectricityConsumptionIndicator(float electricityConsumption);
	/**
	* Create current electricity production indicator
	* Use application's settings using database's interface function (getSettings) to create it
	* Call database's interface function (writeHouseIndicator) to write the new value in database
	* @param electricityProduction = Current house's electricity production value
	*/
	void createElectricityProductionIndicator(float electricityProduction);
	/**
	* Create current electricity ratio indicator
	* Use application's settings using database's interface function (getSettings) to create it
	* Called by createElectricityConsumptionIndicator or/and by createElectricityProductionIndicator
	* Call database's interface function (writeHouseIndicator) to write the new value in database
	*/
	void createElectricityRatioIndicator();
}
