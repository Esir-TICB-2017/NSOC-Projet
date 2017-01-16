public interface ConnectServeur{
    
    /**
    * @param username username to connect
    * @param password password to connect
    * @return true if connected else false
    **/
    public boolean connect(String username, String password);
    
    /**
    * @return true if disconnected else false
    **/
    public boolean disconnect();
    
    
    /**
    * @return true if connected else false
    **/
    public boolean isConnected();
}