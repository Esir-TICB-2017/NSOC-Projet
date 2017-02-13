package webserver.servlets;

import database.ReadInDatabase;
import database.WriteInDatabase;
import org.apache.http.protocol.HTTP;
import org.eclipse.jetty.server.session.JDBCSessionManager;
import webserver.SessionManager;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Created by loulou on 25/01/2017.
 */
public class IsAuthenticatedServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(SessionManager.checkAuthentication(request)){
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
        return;
    }
}
