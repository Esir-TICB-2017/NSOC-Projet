package bacnet;

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
import sensor.sensorClass.ConsumptionSensor;

/**
 * Created by clement on 16/01/2017.
 */


//NOTE:
//ENLEVER LES COMMENTAIRES UNE FOIS CONNECTÉ AU RESEAU WIFI "DOMOTIQUE" DU LABO
//-> SINON CA RETURN UN NOMBRE RANDOM


public class  BacNetToJava implements InterfaceReadBacnet {

    private static IpNetwork network;
    private static LocalDevice localDevice;
    private static int numDevice;
    private static RemoteDevice r;
    private static String typeSensor;
    private static ConsumptionSensor cs;

    public BacNetToJava () {
        cs =  new ConsumptionSensor();
        getSensorValue();
    }

    private static void  getSensorValue() {

            Thread thread = new Thread() {
                double value = 0;
                public void run() {
   //                 connection();
                    while(true) {
   //                     value = getValue();
                        value = Math.random()*100;
                        System.out.print(value);
                        cs.setNewValue(value);
                        try {
                            Thread.sleep(10000);
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


    public static ConsumptionSensor getConsumptionSensor(){
        return cs;
    }

    private static void disconnection(){
        localDevice.terminate();
    }


    private static void connection(){
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

    private static double getValue(){
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