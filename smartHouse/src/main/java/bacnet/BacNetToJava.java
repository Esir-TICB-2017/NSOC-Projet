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
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.util.RequestUtils;
import indicators.Indicator;
import indicators.Indicators;
import sensor.sensorClass.Sensor;
import sensor.sensorClass.Sensors;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Random;

/**
 * Created by clement on 16/01/2017.
 */


//NOTE:
//ENLEVER LES COMMENTAIRES UNE FOIS CONNECTÉ AU RESEAU WIFI "DOMOTIQUE" DU LABO
//-> SINON CA RETURN UN NOMBRE RANDOM


public class BacNetToJava implements InterfaceReadBacnet {

	private IpNetwork network;
	private LocalDevice localDevice;
	private RemoteDevice r;

	private static BacNetToJava bacNetTojava = new BacNetToJava();

	private BacNetToJava() {
		getSensorValue();
	}

	public static BacNetToJava getInstance() {
		return bacNetTojava;
	}

	private void getSensorValue() {

		Thread thread = new Thread() {
			Double value = 0.0;
			Random r = new Random();

			public void run() {
				connection();
				while (true) {
					for (Sensor sensor : Sensors.getInstance().getSensors()) {

						value = getValue(sensor.getBacnetId());
						if (value == -1){
							if(sensor.getStatus()){
								sensor.setStatus(false);
							}
						}
						else{
							if(!sensor.getStatus()){
								sensor.setStatus(true);
							}
						}
						sensor.setNewValue(value);

						Indicator indicator = Indicators.getInstance().getIndicatorByString(sensor.getType());
						indicator.calculateIndicator();
					}
					try {
						Thread.sleep(1000 * 60 * 10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		thread.start();

            /*} catch (BACnetException e) {
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

	private void disconnection() {
		localDevice.terminate();
	}


	private void connection() {
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
			e.printStackTrace();
			System.out.println("Probleme de connexion!");
			disconnection();
		}
	}

	private double getValue(int id) {
		ObjectIdentifier consoBureau = new ObjectIdentifier(ObjectType.analogValue, id);

		String result;
		try {
			result = RequestUtils.getProperty(localDevice, r, consoBureau, PropertyIdentifier.presentValue).toString();
			System.out.println(result);
			return (java.lang.Double.parseDouble(result));
		} catch (BACnetException e) {
			e.printStackTrace();
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