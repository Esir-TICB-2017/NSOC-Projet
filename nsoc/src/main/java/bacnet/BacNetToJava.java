package bacnet;

/**
 * Created by clement on 16/01/2017.
 */
public class BacNetToJava implements InterfaceReadBacnet {

    private IpNetwork network;
    private LocalDevice localDevice;
    private int numDevice;
    private RemoteDevice remote;
    private String typeSensor;

    public BacNetToJava(String type){
        typeSensor = type;
        this.network = new IpNetwork(IpNetwork.DEFAULT_BROADCAST_IP, IpNetwork.DEFAULT_PORT, IpNetwork.DEFAULT_BIND_IP, 1);
        this.localDevice = new LocalDevice(1004, new Transport(network));
        this.numDevice = 9198;
    }


    public double getSensorValue() {

        connection();

        // define the BacNet objects to listen ObjectIdentifier(ObjectType,object_id)
        ObjectIdentifier object = new ObjectIdentifier(ObjectType.analogInput,0);

        String result;
        Double value = null;
        try {
            result = RequestUtils.getProperty(localDevice, remote, object, PropertyIdentifier.presentValue).toString();
            value = Double.valueOf(result);
            System.out.println("Valeur récupérée = " + value);

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
            return -1;
        }
        disconnection();
        return value;
    }


    private void disconnection(){
        localDevice.terminate();
    }


    private void connection(){
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
