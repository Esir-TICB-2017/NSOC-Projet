public interface InterfaceReadServer {

    /**    
    * @return last data of sensors
    * @param type of sensor
    **/
    public float getData(String type);
    
    
    /**
    * @return all indicators
    **/
    public int getIndicators();
    
    
    /**    
    * @return all settings
    **/
    public ArrayList<Float> getSettings();
    
    
    /**
    * @return a list of data
    * @param start the beginning of the period
    * @param end the end of the period
    **/
    public ArrayList<Float> getGraph(Date start, Date end);

}