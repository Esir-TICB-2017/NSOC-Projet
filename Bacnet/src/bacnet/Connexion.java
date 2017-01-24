package bacnet;
import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.Network;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
//import com.serotonin.bacnet4j.obj.BACnetObject;
//import com.serotonin.bacnet4j.type.Encodable;
//import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
//import com.serotonin.bacnet4j.type.primitive.CharacterString.Encodings;
//import com.serotonin.bacnet4j.type.primitive.Real;
//import com.serotonin.bacnet4j.util.PropertyReferences;
//import com.serotonin.bacnet4j.util.PropertyValues;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.bacnet4j.util.RequestUtils;


public class Connexion {
	
	private IpNetwork network;
	private LocalDevice localDevice;
	private RemoteDevice r;
	
	public Connexion() {
		super();
		this.network = new IpNetwork(IpNetwork.DEFAULT_BROADCAST_IP, IpNetwork.DEFAULT_PORT, IpNetwork.DEFAULT_BIND_IP);
		this.localDevice = new LocalDevice(1256, new Transport(network));
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
	
	public double getValue(){
		ObjectIdentifier consoBureau = new ObjectIdentifier(ObjectType.analogValue,20);
		
		String result;
		try {
			result = RequestUtils.getProperty(localDevice, r, consoBureau, PropertyIdentifier.presentValue).toString();
			System.out.println(result);
			return(Double.parseDouble(result));
		} catch (BACnetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			disconnection();
			return -1;
		}


	}
	public void disconnection(){
		localDevice.terminate();
	}
	
	static class Listener extends DeviceEventAdapter {
		public void iAmReceived(RemoteDevice d) {
			System.out.println("I am receiving " + d);
		}
	}
}
