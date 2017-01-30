package bacnet;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.Double;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.util.RequestUtils;
import sensor.sensorClass.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.apache.http.protocol.HTTP.USER_AGENT;

/**
 * Created by clement on 16/01/2017.
 */


//NOTE:
//ENLEVER LES COMMENTAIRES UNE FOIS CONNECTÉ AU RESEAU WIFI "DOMOTIQUE" DU LABO
//-> SINON CA RETURN UN NOMBRE RANDOM


public class  BacNetToJava implements InterfaceReadBacnet {

    private  IpNetwork network;
    private  LocalDevice localDevice;
    private  int numDevice;
    private  RemoteDevice r;
    private  String typeSensor;
    private  ConsumptionSensor cs;
    private  CO2Sensor co2s;
    private  HumiditySensor hs;
    private  ProductionSensor ps;
    private  TemperatureSensor ts;
    private static BacNetToJava bacNetTojava = new BacNetToJava();

    private BacNetToJava () {
        cs =  ConsumptionSensor.getInstance();
        co2s = CO2Sensor.getInstance();
        hs =  HumiditySensor.getInstance();
        ps =  ProductionSensor.getInstance();
        ts =  TemperatureSensor.getInstance();

        getSensorValue();
    }

    public static BacNetToJava getInstance() {
        return bacNetTojava;
    }

    private  void  getSensorValue() {

            Thread thread = new Thread() {
                double value = 0;
                public void run() {
   //                 connection();
                    try {
                       // getTemperature();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    while(true) {
   //                   value = getValue();
                        value = Math.random()*1000;
                        cs.setNewValue(value);
                        value = Math.random()*1000;
                        co2s.setNewValue(value);
                        value = Math.random()*100;
                        hs.setNewValue(value);
                        value = Math.random()*1000;
                        ps.setNewValue(value);
                        /*try {
                            value = getTemperature();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ts.setNewValue(value);*/
                        try {
                            Thread.sleep(1000*10);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            };
            thread.start();

            /*} catch (BACnetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("Problème de récupération de la valeur !");
            }*/

    }
    private double getTemperature() throws Exception {



       String sURL = "https://api.darksky.net/forecast/d1465e3ebc6da2c83fb849178a605d54/48.1113,-1.68,1485796324?lang=fr&units=si";

        URL url = new URL(sURL);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        // Convert to a JSON object to print data
        JsonParser jp = new JsonParser(); //from gson
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
        JsonObject rootobj = root.getAsJsonObject(); //May be an array, may be an object.
        JsonObject currently = rootobj.getAsJsonObject("currently");
        double temperature = currently.get("temperature").getAsDouble();
        return temperature;

    }

    private  void disconnection(){
        localDevice.terminate();
    }


    private  void connection (){
        network = new IpNetwork(IpNetwork.DEFAULT_BROADCAST_IP, IpNetwork.DEFAULT_PORT, IpNetwork.DEFAULT_BIND_IP);
        localDevice = new LocalDevice(1256, new Transport(network));
        // creation du listener
        localDevice.getEventHandler().addListener(new Listener());
        localDevice.terminate();

        //initialisation de la localDevice
        try {
            localDevice.initialize();
            System.out.print("HERE");
            localDevice.sendGlobalBroadcast(new WhoIsRequest());
            Thread.sleep(500);
            r = localDevice.findRemoteDevice(new Address("148.60.19.201", 47808), null, 203);
            System.out.print(r.getObjects());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("Probleme de connexion!");
            disconnection();
        }
    }

    private  double getValue (){
        ObjectIdentifier consoBureau = new ObjectIdentifier(ObjectType.analogValue,20);

        String result;
        try {
            result = RequestUtils.getProperty(localDevice, r, consoBureau, PropertyIdentifier.presentValue).toString();
            System.out.println(result);
            return(java.lang.Double.parseDouble(result));
        } catch (BACnetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            disconnection();
            return -1;
        }


    }
    static class Listener extends DeviceEventAdapter {
        @Override
        public void iAmReceived(RemoteDevice d) {
            System.out.println("I am received" + d);
        }
    }
}