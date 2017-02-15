package webserver.servlets;

import com.google.gson.Gson;
import database.ReadInDatabase;
import org.json.JSONArray;
import webserver.SessionManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Quentin on 31/1/17.
 */

public class GetSettingsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JSONArray settings = null;
        String role = SessionManager.getRole(request);

        if(role.equals("admin") || role.equals("member")){
            settings = ReadInDatabase.getSettings();
        }

        if (settings != null) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(settings);
        } else {
            response.sendError(403);
        }
    }
}