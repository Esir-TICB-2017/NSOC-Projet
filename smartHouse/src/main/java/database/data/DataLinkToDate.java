package database.data;

import java.sql.Timestamp;

/**
 * Created by clement on 25/01/2017.
 */
public class DataLinkToDate {
    private double data;
    private Timestamp date;

    public DataLinkToDate(double data, Timestamp date){
        this.data = data;
        this.date = date;
    }

    public double getData() {
        return data;
    }

    public Timestamp getDate(){
        return date;
    }

}
