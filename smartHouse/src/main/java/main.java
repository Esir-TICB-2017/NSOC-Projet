import bacnet.BacNetToJava;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
//import org.eclipse.jetty.servlet.DefaultServlet;
//import org.eclipse.jetty.servlet.ServletContextHandler;
//import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import sensor.sensorClass.ConsumptionSensor;
//import webserver.GetLastValueServlet;
import webserver.MyWebSocketHandler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by loulou on 18/01/2017.
 */
public class main {
    public static void main (String[] args) throws Exception {


        Server server = new Server(8080);

        String homePath = System.getProperty("user.home");
        String pwdPath = System.getProperty("user.dir") + "/src/main/webapp/";

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(pwdPath);

        HandlerList handlers = new HandlerList();

        WebSocketHandler wsHandler = new WebSocketHandler() {
            @Override
            public void configure(WebSocketServletFactory webSocketServletFactory) {
                webSocketServletFactory.register(MyWebSocketHandler.class);
            }
        };

        handlers.setHandlers(new Handler[]{wsHandler, resourceHandler});
        server.setHandler(handlers);

       // chatWebSocketHandler.setHandler(resourceHandler);

//        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
//        context.setResourceBase(pwdPath);
//        context.setContextPath("/");
//        //server.setHandler(context);
//
//        ServletHolder holderPwd = new ServletHolder("default", DefaultServlet.class);
//        holderPwd.setInitParameter("dirAllowed","true");
//        context.addServlet(holderPwd,"/");
//
//        // add special pathspec of "/home/" content mapped to the homePath
//        ServletHolder holderHome = new ServletHolder("static-home", DefaultServlet.class);
//        holderPwd.setInitParameter("resourceBase",pwdPath);
//        holderHome.setInitParameter("dirAllowed","true");
//        holderHome.setInitParameter("pathInfoOnly","true");
//        context.addServlet(holderHome,"/home/*");

        BacNetToJava physicalSensor = new BacNetToJava();
        ConsumptionSensor sensor = physicalSensor.getConsumptionSensor();
        sensor.getLastValue();
//        Timestamp startDate = new Timestamp(1484757557L*1000);
//        Timestamp endDate = new Timestamp(1484786852L*1000);
//        System.out.println(startDate);
//        ArrayList<HashMap> results = sensor.getValuesOnPeriod(sensor, startDate, endDate);
//        System.out.println(results);
//        GetLastValueServlet getLastValueServlet = new GetLastValueServlet(sensor);
//        ServletHolder resourceHolder = new ServletHolder(getLastValueServlet);
//        context.addServlet(resourceHolder , "/getLastValue");

        server.start();
        server.join();


    }
}
