import bacnet.BacNetToJava;
import com.google.gson.Gson;
import database.ReadInDatabase;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.JDBCSessionIdManager;
import org.eclipse.jetty.server.session.JDBCSessionManager;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Credential;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.json.JSONObject;
import sensor.sensorClass.ConsumptionSensor;
import webserver.*;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.servlet.*;
import javax.servlet.http.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.HashMap;

import static java.lang.System.out;


/**
 * Created by loulou on 18/01/2017.
 */
public class main {
    public static void main (String[] args) throws Exception {

        //****WAITING FOR SSL CERTIFICATE****//
//        String jettyDistKeystore = System.getProperty("user.home") + "/projets_2016/certificates/server.csr";
//        String keystorePath = System.getProperty(
//                "example.keystore", jettyDistKeystore);
//        File keystoreFile = new File(keystorePath);
//        if (!keystoreFile.exists())
//        {
//            throw new FileNotFoundException(keystoreFile.getAbsolutePath());
//        }

        BacNetToJava physicalSensor = new BacNetToJava();
        ConsumptionSensor sensor = physicalSensor.getConsumptionSensor();
        sensor.getLastValue();
        // Get webapp directory
        String pwdPath = System.getProperty("user.dir") + "/src/main/webapp/";
        String keyPath = System.getProperty("user.home" + "/projets_2016/certificates/");

        Server server = new Server(8080);

//****WAITING FOR SSL CERTIFICATE****//
//        HttpConfiguration http_config = new HttpConfiguration();
//        http_config.setSecureScheme("https");
//        http_config.setSecurePort(8443);
//        http_config.setOutputBufferSize(32768);


//        SslContextFactory sslContextFactory = new SslContextFactory();
//        sslContextFactory.setKeyStorePath(keystoreFile.getAbsolutePath());
//        sslContextFactory.setKeyStorePassword("!!-Projetns0c-!!");
//        sslContextFactory.setKeyManagerPassword("!!-Projetns0c-!!");
//
//        HttpConfiguration https_config = new HttpConfiguration(http_config);
//        SecureRequestCustomizer src = new SecureRequestCustomizer();
//        https_config.addCustomizer(src);
//
//        ServerConnector https = new ServerConnector(server,
//                new SslConnectionFactory(sslContextFactory,HttpVersion.HTTP_1_1.asString()),
//                new HttpConnectionFactory(https_config));
//        https.setPort(8443);
//        https.setIdleTimeout(500000);

//        ServerConnector http = new ServerConnector(server,
//                new HttpConnectionFactory(http_config));
//        http.setPort(8080);
//        http.setIdleTimeout(30000);

        // Set the connectors
//        server.setConnectors(new Connector[] { http });


        // Static files handler
//        ResourceHandler resourceHandler = new ResourceHandler();
//        resourceHandler.setResourceBase("src/main/webapp/");

        // WebSocket Handler
        WebSocketHandler wsHandler = new WebSocketHandler() {
            @Override
            public void configure(WebSocketServletFactory webSocketServletFactory) {
                webSocketServletFactory.register(MyWebSocketHandler.class);
            }
        };

        WebAppContext context = new WebAppContext();
        context.setDescriptor("webapp/WEB-INF/web.xml");
        context.setResourceBase("../smartHouse/src/main/webapp");
        context.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
        context.setWelcomeFiles(new String[]{ "index.html" });
        context.setParentLoaderPriority(true);
        context.addServlet(new ServletHolder(new SigninTokenServlet()), "/tokensignin");
        context.addServlet(new ServletHolder(new LogoutServlet()), "/logout");

        context.addFilter(HelloPrintingFilter.class, "/api/*", EnumSet.of(DispatcherType.REQUEST));


        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{context, wsHandler });
        server.setHandler(handlers);


        server.start();
        server.join();


    }

    public static class HelloPrintingFilter implements Filter {
        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
                throws IOException, ServletException {
            HttpServletRequest request = (HttpServletRequest) servletRequest;



            HttpSession session = request.getSession(false);
            if(session!=null){
                String name=(String)session.getAttribute("name");
                chain.doFilter(servletRequest, servletResponse);
                out.println("Hello, "+name+" Welcome to Profile");

            }
            else{
                out.println("Please login first");
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                response.sendRedirect("/");
            }
            return;
        }

        @Override
        public void init(FilterConfig arg0) throws ServletException {

        }

        @Override
        public void destroy() {}
    }
}
