package computeAggregatedData;

/**
* This class create indicator(s) about house's health
*/

public interface InterfaceComputeAggregatedData {
	/**
	 * Create current Global indicator
	 * Use Use application's settings using database's interface function (getSettings) to create it
	 */
	void createGlobalIndicator();

	/**
	* Create current CO2 indicator
	* Use application's settings using database's interface function (getSettings) to create it
	* @param currentCo2 = Current CO2 sensor's value
	*/
	void createCO2Indicator(float currentCo2);
	/**
	* Create current temparature indicator
	* Use application's settings using database's interface function (getSettings) to create it
	* Call database's interface function (writeHouseIndicator) to write the new value in database
	* @param currentTemperature = Current temperature sensor's value
	*/
	void createTemperatureIndicator(float currentTemperature);
	/**
	* Create current humidity indicator
	* Use application's settings using database's interface function (getSettings) to create it
	* Call database's interface function (writeHouseIndicator) to write the new value in database
	* @param currentHumidity = Current humidity sensor's value
	*/
	void createHumidityIndicator(float currentHumidity);
	/**
	* Create current electricity consumption indicator
	* Use application's settings using database's interface function (getSettings) to create it
	* Call database's interface function (writeHouseIndicator) to write the new value in database
	* @param currentElectricityConsumption = Current house's electricity consumption value
	*/
	void createElectricityConsumptionIndicator(float currentElectricityConsumption);
	/**
	* Create current electricity production indicator
	* Use application's settings using database's interface function (getSettings) to create it
	* Call database's interface function (writeHouseIndicator) to write the new value in database
	* @param currentElectricityProduction = Current house's electricity production value
	*/
	void createElectricityProductionIndicator(float currentElectricityProduction);
	/**
	* Create current electricity ratio indicator
	* Use application's settings using database's interface function (getSettings) to create it
	* Called by createElectricityConsumptionIndicator or/and by createElectricityProductionIndicator
	* Call database's interface function (writeHouseIndicator) to write the new value in database
	*/
	void createElectricityRatioIndicator();
}
