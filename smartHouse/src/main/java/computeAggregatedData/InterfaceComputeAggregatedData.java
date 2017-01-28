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
	* Use application's settings using database's interface function (getSettings) to create it*
	*/
	void createCO2Indicator();
	/**
	* Create current temparature indicator
	* Use application's settings using database's interface function (getSettings) to create it
	* Call database's interface function (writeHouseIndicator) to write the new value in database
	*/
	void createTemperatureIndicator();
	/**
	* Create current humidity indicator
	* Use application's settings using database's interface function (getSettings) to create it
	* Call database's interface function (writeHouseIndicator) to write the new value in database
	*/
	void createHumidityIndicator();
	/**
	* Create current electricity consumption indicator
	* Use application's settings using database's interface function (getSettings) to create it
	* Call database's interface function (writeHouseIndicator) to write the new value in database
	*/
	void createElectricityConsumptionIndicator();
	/**
	* Create current electricity production indicator
	* Use application's settings using database's interface function (getSettings) to create it
	* Call database's interface function (writeHouseIndicator) to write the new value in database
	*/
	void createElectricityProductionIndicator();

}
