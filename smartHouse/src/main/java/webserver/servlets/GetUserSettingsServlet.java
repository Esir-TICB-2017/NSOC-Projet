package webserver.servlets;

import com.google.gson.Gson;
import database.ReadInDatabase;
import database.WriteInDatabase;
import org.json.JSONArray;
import webserver.SessionManager;

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

            String userid = SessionManager.getUserId(request);

            JSONArray userSettings = ReadInDatabase.getUserSettings(userid);

            //if users already have saved settings in db, get it
            //if not, get default ones
            if (userSettings != null) {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(userSettings);
            } else {
                JSONArray defaultSettings = ReadInDatabase.getDefaultSettings();
                WriteInDatabase.resetUserSettings(userid);

                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(defaultSettings);
            }
        }
}
