public interface InterfaceHumiditySensor {
    
    /**
    * @return a list of data
    * @param start the beginning of the period
    * @param end the end of the period
    **/
    public ArrayList<Float> getHumidityOnPeriod(Date start, Date end);
    
    /**    
    * @return the last data
    **/
    public float getLastHumidity();


}