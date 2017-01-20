import bacnet.BacNetToJava;
import database.ConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import sensor.sensorClass.ConsumptionSensor;


/**
 * Created by loulou on 18/01/2017.
 */
public class main {
    public static void main (String[] args) throws Exception {

        Server server = new Server(8080);
        ConnectionManager.initializeConnection();

        String homePath = System.getProperty("user.home");
        String pwdPath = System.getProperty("user.dir") + "/src/main/webapp/";
        System.out.println(pwdPath);


        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setResourceBase(pwdPath);
        context.setContextPath("/");
        server.setHandler(context);

        ServletHolder holderPwd = new ServletHolder("default", DefaultServlet.class);
        holderPwd.setInitParameter("dirAllowed","true");
        context.addServlet(holderPwd,"/");

        // add special pathspec of "/home/" content mapped to the homePath
        ServletHolder holderHome = new ServletHolder("static-home", DefaultServlet.class);
        holderPwd.setInitParameter("resourceBase",pwdPath);
        holderHome.setInitParameter("dirAllowed","true");
        holderHome.setInitParameter("pathInfoOnly","true");
        context.addServlet(holderHome,"/home/*");



        server.start();
        server.join();

        BacNetToJava physicalSensor = new BacNetToJava();
        ConsumptionSensor sensor = physicalSensor.getConsumptionSensor();

        while(true) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(sensor.getLastValue());
        }


    }
}
