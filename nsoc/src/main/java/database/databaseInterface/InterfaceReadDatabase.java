package database.databaseInterface;

import javax.json.Json;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by mathieu on 16/01/2017.
 */
public interface InterfaceReadDatabase {
    /**
     * Get a specific setting stored in database and return it
     * @param settingName: The name of the wanted setting
     * @return : Setting object in JSON
     **/
    Json getSettings (String settingName);
    /**
     * Permet de lire dans la base de donnée la dernière valeur stockée du type de capteur saisi par "_init()"
     * Construit alors la requete SQL adaptée
     * @return : retourne les settings Cette valeur est retournée sous format JSON
     **/
    String getLastValue();

    /**
     * Permet de lire dans la base de donnée les "nbValue" dernières valeurs stockées du type de capteur saisi par "_init()"
     * Construit alors la requete SQL adaptée
     * @param nbValue = Nombre du nombres des dernières valeurs à retourner
     * @return Ces valeurs sont retournées sous format JSON
     **/
    String getNbValue(int nbValue);

    /**
     * @param date = Date à partir de laquelle retourner les valeurs
     * Permet de lire dans la base de donnée les dernières valeurs stockées depuis la date saisie du type de capteur saisi par "_init()"
     * Construit alors la requete SQL adaptée
     * @return Ces valeurs sont retournées sous format JSON
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

    ArrayList<Float> getLastNbIndicator(String indicatorType, int nbValue);

    /**
     * Lis dans la base de données les indicateurs d'un certain type donnée dans une période donnée et les retourne
     * Construit alors la requete SQL adaptée
     * @param indicatorType = Type d'indicateur dont on veut obtenir des valeurs. ex : Air quality, CO2, temparature, ...
     * @param start = Date de début de la période
     * @param end = Date de fin de la période
     * @return : Un tableau avec toutes les valeurs trouvées
     **/
    ArrayList<Float> getIndicatorPeriod(String indicatorType, Date start, Date end);
}
