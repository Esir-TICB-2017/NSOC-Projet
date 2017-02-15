package webserver.servlets;

import com.google.gson.Gson;
import database.ReadInDatabase;
import org.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Quentin on 9/2/17.
 */
public class GetUserSettingsServlet extends HttpServlet {

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            String userid = (String) request.getSession().getAttribute("userId");

            JSONArray userSettings = ReadInDatabase.getUserSettings(userid);

            //if users already have saved settings in db, get it
            //if not, get default ones
            if (userSettings != null) {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(userSettings);
            } else {
                JSONArray defaultSettings = ReadInDatabase.getDefaultSettings();

                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(defaultSettings);
            }
        }
}
