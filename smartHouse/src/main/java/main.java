import bacnet.BacNetToJava;
import database.ReadInDatabase;
import indicators.Indicator;
import indicators.Indicators;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import sensor.sensorClass.Sensor;
import sensor.sensorClass.Sensors;
import webserver.*;
import webserver.servlets.*;

import javax.servlet.DispatcherType;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;


/**
 * Created by loulou on 18/01/2017.
 */
public class main {
	public static void main(String[] args) throws Exception {

		Sensors.getInstance().initSensors();
		Indicators.getInstance().initIndicators();
		JSONArray settings = ReadInDatabase.getSettings();

		//BacNetToJava.getInstance();

		// Get webapp directory
		String pwdPath = System.getProperty("user.dir") + "/src/main/webapp/";
		String keyPath = System.getProperty("user.home") + "/NSOC/NSOC-Projet/keystore/";

		Thread thread = new Thread() {
			Double value = 0.0;
			Random r = new Random();

			public void run() {
				double value = 0;
				Random r = new Random();
				// Create fake sensors instance
				List<Sensor> sensors = new ArrayList();
				ArrayList<JSONObject> sensorsList = ReadInDatabase.getAllSensors();
				for (JSONObject sensorAttributes : sensorsList) {
					Sensor sensor = new Sensor(sensorAttributes.getString("name"), sensorAttributes.getInt("id"), sensorAttributes.getString("unit"), sensorAttributes.getInt("bacnetId"), sensorAttributes.getBoolean("status"));
					sensors.add(sensor);
				}
				//Fill database with normal distributed values
				while (true) {
					for (Sensor sensor : sensors) {
						switch (sensor.getType()) {
							case "consumption":
								value = r.nextGaussian() * 1000 + 2000;
								sensor.setNewValue(value);
								break;
							case "co2":
								value = r.nextGaussian() * 400 + 600;
								if (value > 2000) {
									value = 2000;
								}
								if (value < 0) {
									value = 0;
								}
								sensor.setNewValue(value);

								break;
							case "production":
								value = r.nextGaussian() * 1000 + 2000;
								sensor.setNewValue(value);
								break;
							case "humidity":
								value = r.nextGaussian() * 10 + 40;
								if (value > 100) {
									value = 100;
								}
								if (value < 0) {
									value = 0;
								}
								sensor.setNewValue(value);
								break;
							case "temperature":
								value = r.nextGaussian() * 2 + 20;
								sensor.setNewValue(value);
								break;
							default:
								break;
						}
					}
					try {
						//Fill DB every 10 minutes
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		//thread.start();

		Server server = new Server();
		ServerConnector connector = new ServerConnector(server);
		connector.setPort(8080);
		/*
		HttpConfiguration https = new HttpConfiguration();
        https.addCustomizer(new SecureRequestCustomizer());
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath(keyPath + "keystore.jks");
        sslContextFactory.setKeyStorePassword("projetnsoc");
        sslContextFactory.setKeyManagerPassword("projetnsoc");
        ServerConnector sslConnector = new ServerConnector(server,
                new SslConnectionFactory(sslContextFactory, "http/1.1"),
                new HttpConnectionFactory(https));
        sslConnector.setPort(9998);
        server.setConnectors(new Connector[] { connector, sslConnector });
    */
		server.setConnectors(new Connector[]{connector});
		// WebSocket Handler
		org.eclipse.jetty.websocket.server.WebSocketHandler wsHandler = new org.eclipse.jetty.websocket.server.WebSocketHandler() {
			@Override
			public void configure(WebSocketServletFactory webSocketServletFactory) {
				webSocketServletFactory.register(WebSocketHandler.class);
			}
		};

		WebAppContext context = new WebAppContext();
		context.setDescriptor("webapp/WEB-INF/web.xml");
		context.setResourceBase(pwdPath);
		context.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
		context.setWelcomeFiles(new String[]{"index.html"});
		context.setParentLoaderPriority(true);
		context.addServlet(new ServletHolder(new LoginServlet()), "/login");
		context.addServlet(new ServletHolder(new LogoutServlet()), "/logout");
		context.addServlet(new ServletHolder(new GetValuesOnPeriodServlet()), "/getValuesOverPeriod");
		context.addServlet(new ServletHolder(new GetIndicatorsOnPeriodServlet()), "/getIndicatorsOverPeriod");
		context.addServlet(new ServletHolder(new GetSettingsServlet()), "/getSettings");
		context.addServlet(new ServletHolder(new GetUserSettingsServlet()), "/getUserSettings");
		context.addServlet(new ServletHolder(new PostSettingsServlet()), "/postSettings");
		context.addServlet(new ServletHolder(new GetFirstDataServlet()), "/getFirstData");

		context.addFilter(RequestFilter.class, "*", EnumSet.of(DispatcherType.REQUEST));

		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[]{wsHandler, context});
		server.setHandler(handlers);
		server.start();

		server.join();
	}
}
