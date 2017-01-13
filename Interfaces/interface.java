/*
*
		DATABASE
	by : Badane & Cheval
*
*/

//////////Init
public interface Init
{
	/*
	@type = Type du capteur qui instancie la class
	# Permet de fournir le type de capteur qui instancie la class et donc de selectionner par la suite le type des valeurs à retourner
	*/
	void _init(String type);
}

//////////Subscribe
public interface Subscribe 
{
	/*
	# Permet de s'abonner et donc d'être notifié lors de tout ajout d'une valeur brut dans la base de données
	*/
	void subscribeAllData();

	/*
	@type = Type de valeur dont on veut être notifié. ex : CO2, Temp...
	# Permet de s'abonner et donc d'être notifié lors de tout ajout d'une valeur brut du type saisi dans la base de données
	*/
	void subscribeTypeData(SensorType type);

	/*
	# Permet de s'abonner et donc d'être notifié lors de tout ajout d'un indicateur dans la base de données
	*/
	void subscribeAllIndicator();

	/*
	@type = Type d'indicateur dont on veut être notifié. ex : Air quality...
	# Permet de s'abonner et donc d'être notifié lors de tout ajout d'un indicateur du type saisi dans la base de données
	*/
	void subscribeTypeIndicator(IndicatorType type);
}
//////////Write
public interface Write 
{
	/*
	@value = Valeur du capteur
	# Permet d'écrire dans la base de donnée une valeur capteur brute du type saisie par "_init()"
	# Construit alors la requete SQL adaptée
	*/
	void writeSensorValue(float value);

	/*
	@type = Type d'indicateur fourni. ex : Air quality...
	@value = Valeur de l'indicateur
	# Permet d'écrire dans la base de donnée un indicateur 
	# Construit alors la requete SQL adaptée
	*/
	void writeHouseIndicator(IndicatorType type, float value);

	/*
	@conf = Fichier JSON comportant toutes les valeurs nécéssaires à la créations des indicateurs
	# Permet d'écrire dans la base de donnée la configuration propre à la création des indicateurs
	*/
	void writeIndicatorConf(String conf);

}

///////////Read
public interface Read
{
	/*
	# Permet de lire dans la base de donnée la dernière valeur stockée du type de capteur saisi par "_init()" 
	# Construit alors la requete SQL adaptée
	# Cette valeur est retournée sous format JSON
	*/
	String getLastValue();

	/*
	@nbValue = Nombre du nombres des dernières valeurs à retourner
	# Permet de lire dans la base de donnée les "nbValue" dernières valeurs stockées du type de capteur saisi par "_init()"
	# Construit alors la requete SQL adaptée
	# Ces valeurs sont retournées sous format JSON
	*/
	String getNbValue(int nbValue);

	/*
	@date = Date à partir de laquelle retourner les valeurs
	# Permet de lire dans la base de donnée les dernières valeurs stockées depuis la date saisie du type de capteur saisi par "_init()"
	# Construit alors la requete SQL adaptée
	# Ces valeurs sont retournées sous format JSON
	*/
	String getValueFrom(Date date);

	/*
	@dateS = Date à partir de laquelle retourner les valeurs
	@dateE = Date jusqu'à laquelle retourner les valeurs
	# Permet de lire dans la base de donnée les valeurs stockées entre les deux dates saisies du type de capteur saisi par "_init()"
	# Construit alors la requete SQL adaptée
	# Ces valeurs sont retournées sous format JSON
	*/
	String getValuePeriod(Date dateS, Date dateE);

	/*
	@type = Type d'indicateur à fourni. ex : Air quality...
	# Permet de lire dans la base de donnée le dernier indicateur stocké du type saisi 
	# Construit alors la requete SQL adaptée
	# Cette valeur est retournée sous format JSON
	*/
	String getLastIndicator(IndicatorType type);

	/*
	@type = Type d'indicateur à fourni. ex : Air quality...
	@nbValue = Nombre du nombres des dernières valeurs à retourner
	# Permet de lire dans la base de donnée les "nbValue" derniers indicateurs du type saisi
	# Construit alors la requete SQL adaptée
	# Ces valeurs sont retournées sous format JSON
	*/
	String getNbIndicator(IndicatorType type, int nbValue);

	/*
	@type = Type d'indicateur à fourni. ex : Air quality...
	@date = Date à partir de laquelle retourner les valeurs
	# Permet de lire dans la base de donnée les derniers indicateurs stockés depuis la date saisie du type saisi
	# Construit alors la requete SQL adaptée
	# Ces valeurs sont retournées sous format JSON
	*/
	String getIndicatorFrom(IndicatorType type, Date date);

	/*
	@type = Type d'indicateur à fourni. ex : Air quality...
	@dateS = Date à partir de laquelle retourner les valeurs
	@dateE = Date jusqu'à laquelle retourner les valeurs
	# Permet de lire dans la base de donnée les indicateurs stockés entre les deux dates saisies du type saisi
	# Construit alors la requete SQL adaptée
	# Ces valeurs sont retournées sous format JSON
	*/
	String getIndicatorPeriod(IndicatorType type, Date dateS, Date dateE);



}
