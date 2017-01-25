package webserver;

import database.ReadInDatabase;
import database.WriteInDatabase;
import org.apache.http.protocol.HTTP;
import org.eclipse.jetty.server.session.JDBCSessionManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
