package webserver.servlets;

import com.google.gson.Gson;
import database.ReadInDatabase;
import org.json.JSONArray;
import org.json.JSONObject;
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

        JSONObject allObject = new JSONObject();
        JSONArray settings = new JSONArray();
        JSONArray users = new JSONArray();

        String role = SessionManager.getRole(request);

        if(role.equals("admin") || role.equals("member")){
            settings = ReadInDatabase.getSettings();
            allObject.put("settings", settings);
            users = ReadInDatabase.getUsers();
            allObject.put("users", users);

        }

        if ( allObject!= null) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(allObject);
        } else {
            response.sendError(403);
        }
    }
}