package bacnet;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.util.RequestUtils;
import sensor.sensorClass.ConsumptionSensor;

/**
 * Created by clement on 16/01/2017.
 */
public  class  BacNetToJava implements InterfaceReadBacnet {

    private static IpNetwork network;
    private static LocalDevice localDevice;
    private static int numDevice;
    private static RemoteDevice remote;
    private static String typeSensor;
    private static ConsumptionSensor cs;

    public static void main(String arg){
        network = new IpNetwork(IpNetwork.DEFAULT_BROADCAST_IP, IpNetwork.DEFAULT_PORT, IpNetwork.DEFAULT_BIND_IP, 1);
        localDevice = new LocalDevice(1004, new Transport(network));
        numDevice = 9198;
        cs = new ConsumptionSensor();
        getSensorValue();

    }



    private static void  getSensorValue() {

        connection();
        String result;
        Double value = null;
        while(true){
            // define the BacNet objects to listen ObjectIdentifier(ObjectType,object_id)
            ObjectIdentifier object = new ObjectIdentifier(ObjectType.analogInput,0);


            try {
                result = RequestUtils.getProperty(localDevice, remote, object, PropertyIdentifier.presentValue).toString();
                value = Double.valueOf(result);
                cs.setNewValue(value);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } catch (BACnetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("Problème de récupération de la valeur !");
            }
        }
    }



    private static void disconnection(){
        localDevice.terminate();
    }


    private static void connection(){
        // cr�ation du listener
        localDevice.getEventHandler().addListener(new Listener());
        localDevice.terminate();

        //initaialisation de la localDevice
        try {
            localDevice.initialize();
            localDevice.sendGlobalBroadcast(new WhoIsRequest());
            Thread.sleep(1000);
            remote = localDevice.getRemoteDevice(numDevice);
            System.out.println("");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Probl�me de connexion avec la maquette !");
        }
    }

    static class Listener extends DeviceEventAdapter {
        @Override
        public void iAmReceived(RemoteDevice d) {
            System.out.println("I am received" + d);
        }
    }
}
