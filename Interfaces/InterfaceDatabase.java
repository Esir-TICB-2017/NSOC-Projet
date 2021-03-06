// Init
public interface Init
{
	/**
	* @param type = Type du capteur de la nouvelle instance
	* Fournit le type de capteur qui instancie un objet de la classe
	* Permet de spécifier aux fonctions de la classe le type des valeurs à écrire et retourner dans la base de données
	**/
	void _init(String type);
}

// Subscribe
public interface Subscribe
{
	/**
	* Permet de s'abonner et donc d'être notifié lors de tout ajout d'une valeur brut dans la base de données
	**/
	void subscribeAllData();

	/**
	* @param type = Type de valeur dont on veut être notifié. ex : CO2, Temp...
	* Permet de s'abonner et donc d'être notifié lors de tout ajout d'une valeur brut du type saisi dans la base de données
	**/
	void subscribeTypeData(SensorType type);

	/**
	* Permet de s'abonner et donc d'être notifié lors de tout ajout d'un indicateur dans la base de données
	**/
	void subscribeAllIndicator();

	/**
	* @param indicatorType = Type d'indicateur dont on veut être notifié. ex : Air quality, CO2, temparature, ...
	* Permet de s'abonner et donc d'être notifié lors de tout ajout d'un indicateur du type saisi dans la base de données
	**/
	void subscribeTypeIndicator(String indicatorType);
}

// Write
public interface Write
{
	/**
	* @param  = Valeur du capteur
	* Permet d'écrire dans la base de donnée une valeur capteur brute du type saisie par "_init()"
	* Construit alors la requete SQL adaptée
	**/
	void writeSensorValue(float value);

	/**
	* @param indicatorType = Type d'indicateur dont on veut écrire une nouvelle valeur. ex : Air quality, CO2, temparature, ...
	* @param value = Valeur de l'indicateur
	* Permet d'écrire dans la base de donnée un indicateur
	* Construit alors la requete SQL adaptée
	**/
	void writeHouseIndicator(String indicatorType, float value);

	/**
	* Write the new application's settings in database
	* @param settings = JSON's file including all application's settings
	**/
	void writeSettings(JSON settings);

}

// Read
public interface Read
{
	/**
	* Get a specific setting stored in database and return it
	* @param settingName: The name of the wanted setting
	* @return : Setting object in JSON
	**/
	JSON getSettings (String settingName);
	/**
	* Permet de lire dans la base de donnée la dernière valeur stockée du type de capteur saisi par "_init()"
	* Construit alors la requete SQL adaptée
	* Cette valeur est retournée sous format JSON
	**/
	String getLastValue();

	/**
	* Permet de lire dans la base de donnée les "nbValue" dernières valeurs stockées du type de capteur saisi par "_init()"
	* Construit alors la requete SQL adaptée
	* @param nbValue = Nombre du nombres des dernières valeurs à retourner
	* Ces valeurs sont retournées sous format JSON
	**/
	String getNbValue(int nbValue);

	/**
	* @param date = Date à partir de laquelle retourner les valeurs
	* Permet de lire dans la base de donnée les dernières valeurs stockées depuis la date saisie du type de capteur saisi par "_init()"
	* Construit alors la requete SQL adaptée
	* Ces valeurs sont retournées sous format JSON
	**/
	String getValueFrom(Date date);

	/**
	* @param start = Date à partir de laquelle retourner les valeurs
	* @param end = Date jusqu'à laquelle retourner les valeurs
	* Permet de lire dans la base de donnée les valeurs stockées entre les deux dates saisies du type de capteur saisi par "_init()"
	* Construit alors la requete SQL adaptée
	* Ces valeurs sont retournées sous format JSON
	**/
	String getValuePeriod(Date start, Date end);

	/**
	* @param indicatorType = Type d'indicateur dont on veut obtenir la dernière valeur. ex : Air quality, CO2, temparature, ...
	* Permet de lire dans la base de donnée le dernier indicateur stocké du type saisi
	* Construit alors la requete SQL adaptée
	* @return : Retourne la valeur de cet indicateur
	**/
	float getLastIndicator(String indicatorType);

	/**
	* @param indicatorType = Type d'indicateur dont on veut obtenir des valeurs. ex : Air quality, CO2, temparature, ...
	* @param nbValue = Nombre du nombres des dernières valeurs à retourner
	* Permet de lire dans la base de donnée les "nbValue" derniers indicateurs du type saisi
	* Construit alors la requete SQL adaptée
	* @return : Un tableau avec toutes les valeurs trouvées
	**/
<foat> getLastNbIndicator(String indicatorType, int nbValue);

	/**
	* Lis dans la base de données les indicateurs d'un certain type donnée dans une période donnée et les retourne
	* Construit alors la requete SQL adaptée
	* @param indicatorType = Type d'indicateur dont on veut obtenir des valeurs. ex : Air quality, CO2, temparature, ...
	* @param start = Date de début de la période
	* @param end = Date de fin de la période
	* @return : Un tableau avec toutes les valeurs trouvées
	**/
	<float> getIndicatorPeriod(String indicatorType, Date start, Date end);
}
