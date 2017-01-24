package database.databaseInterface;

/**
 * Created by mathieu on 16/01/2017.
 */
public interface InterfaceSubscibeDatabase {
    /**
     * Permet de s'abonner et donc d'être notifié lors de tout ajout d'une valeur brut dans la base de données
     **/
    //void subscribeAllData();

    /**
     * @param type = Type de valeur dont on veut être notifié. ex : CO2, Temp...
     * Permet de s'abonner et donc d'être notifié lors de tout ajout d'une valeur brut du type saisi dans la base de données
     **/
    //void subscribeTypeData(String type);

    /**
     * Permet de s'abonner et donc d'être notifié lors de tout ajout d'un indicateur dans la base de données
     **/
    //void subscribeAllIndicator();

    /**
     * @param indicatorType = Type d'indicateur dont on veut être notifié. ex : Air quality, CO2, temparature, ...
     * Permet de s'abonner et donc d'être notifié lors de tout ajout d'un indicateur du type saisi dans la base de données
     **/
    //void subscribeTypeIndicator(String indicatorType);
}
