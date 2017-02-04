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
import sensor.sensorClass.Sensor;
import sensor.sensorClass.Sensors;
import webserver.*;

import javax.servlet.DispatcherType;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by loulou on 18/01/2017.
 */
public class main {
	public static void main(String[] args) throws Exception {


		Sensors sensors = Sensors.getInstance();
		Map<String, Integer> sensorsList = ReadInDatabase.getAllSensorsName();
		Iterator its = sensorsList.entrySet().iterator();
		while (its.hasNext()) {
			Map.Entry pair = (Map.Entry) its.next();
			Sensor sensor = new Sensor((String) pair.getKey(), (Integer) pair.getValue());
			sensors.addSensor(sensor);
		}
		Indicators indicators = Indicators.getInstance();
		Map<String, Integer> indicatorsList = ReadInDatabase.getAllIndicatorsName();
		Iterator iti = indicatorsList.entrySet().iterator();
		while (iti.hasNext()) {
			Map.Entry pair = (Map.Entry) iti.next();
			Indicator indicator = new Indicator((String) pair.getKey(), (Integer) pair.getValue());
			indicators.addIndicator(indicator);
		}
		BacNetToJava.getInstance();

		Thread thread = new Thread() {
			public void run() {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				while(true) {
					Indicator indicator = Indicators.getInstance().getIndicatorByString("global");
					indicator.calculateIndicator();

				}
			}
		};
		thread.start();


		// Get webapp directory
		String pwdPath = System.getProperty("user.dir") + "/src/main/webapp/";
		String keyPath = System.getProperty("user.home") + "/NSOC/NSOC-Projet/keystore/";

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
		context.addServlet(new ServletHolder(new IsAuthenticatedServlet()), "/isAuthenticated");
		context.addServlet(new ServletHolder(new LoginServlet()), "/login");
		context.addServlet(new ServletHolder(new LogoutServlet()), "/logout");
		context.addServlet(new ServletHolder(new GetValuesOnPeriodServlet()), "/getValuesOverPeriod");
		context.addServlet(new ServletHolder(new GetLastIndicatorsServlet()), "/getLastIndicators");
		context.addServlet(new ServletHolder(new GetIndicatorsOnPeriodServlet()), "/getIndicatorsOverPeriod");
		context.addServlet(new ServletHolder(new GetSettingsServlet()), "/getSettings");
		context.addServlet(new ServletHolder(new PostSettingsServlet()), "/postSettings");

		context.addFilter(RequestFilter.class, "*", EnumSet.of(DispatcherType.REQUEST));

		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[]{wsHandler, context});
		server.setHandler(handlers);
		server.start();


		server.join();
	}


}
