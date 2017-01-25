import bacnet.BacNetToJava;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import sensor.sensorClass.ConsumptionSensor;
import webserver.*;

import javax.servlet.*;
import java.util.EnumSet;


/**
 * Created by loulou on 18/01/2017.
 */
public class main {
    public static void main (String[] args) throws Exception {

        BacNetToJava physicalSensor = BacNetToJava.getInstance();
        ConsumptionSensor sensor = physicalSensor.getConsumptionSensor();
        sensor.getLastValue();
        // Get webapp directory
        String pwdPath = System.getProperty("user.dir") + "/src/main/webapp/";
        String keyPath = System.getProperty("user.home") + "/projets_2016/NSOC-Projet/keystore/";
        System.out.println(keyPath);

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
        server.setConnectors(new Connector[] { connector });
        // WebSocket Handler
        WebSocketHandler wsHandler = new WebSocketHandler() {
            @Override
            public void configure(WebSocketServletFactory webSocketServletFactory) {
                webSocketServletFactory.register(MyWebSocketHandler.class);
            }
        };

        WebAppContext context = new WebAppContext();
        context.setDescriptor("webapp/WEB-INF/web.xml");
        context.setResourceBase(pwdPath);
        context.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
        context.setWelcomeFiles(new String[]{ "index.html" });
        context.setParentLoaderPriority(true);
        context.addServlet(new ServletHolder(new IsAuthenticatedServlet()), "/isAuthenticated");
        context.addServlet(new ServletHolder(new LoginServlet()), "/login");
        context.addServlet(new ServletHolder(new LogoutServlet()), "/logout");
        context.addServlet(new ServletHolder(new GetValuesOnPeriodServlet(sensor)), "/data");

        context.addFilter(RequestFilter.class, "*", EnumSet.of(DispatcherType.REQUEST));


        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{context, wsHandler });
        server.setHandler(handlers);
        server.start();
        server.join();


    }


}
