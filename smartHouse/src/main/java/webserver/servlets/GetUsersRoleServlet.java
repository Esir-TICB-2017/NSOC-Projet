package webserver.servlets;

import database.ReadInDatabase;
import org.json.JSONArray;
import webserver.SessionManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by clement on 13/02/2017.
 */
public class GetUsersRoleServlet  extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JSONArray users = null;
        String role = SessionManager.getRole(request);

        if(role.equals("admin") || role.equals("member")){
            users = ReadInDatabase.getUsers();
        }

        if (users != null) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(users);
        } else {
            response.sendError(403);
        }
    }
}
