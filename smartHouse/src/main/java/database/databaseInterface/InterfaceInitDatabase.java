package database.databaseInterface;

/**
 * Created by mathieu on 16/01/2017.
 */
public interface InterfaceInitDatabase {
    /**
     * @param type = Type du capteur de la nouvelle instance
     * Fournit le type de capteur qui instancie un objet de la classe
     * Permet de spécifier aux fonctions de la classe le type des valeurs à écrire et retourner dans la base de données
     **/
    void _init(String type);
}
