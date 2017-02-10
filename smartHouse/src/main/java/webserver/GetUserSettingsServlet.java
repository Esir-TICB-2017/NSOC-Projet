package webserver;

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
            JSONArray userSettings;
            String userid = (String) request.getSession().getAttribute("userId");

            userSettings = ReadInDatabase.getUserSettings(userid);

            if (userSettings != null) {
                Gson gson = new Gson();
                String json = gson.toJson(userSettings).toString();
                response.setContentType("text/HTML");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(json);
            } else {
                response.sendError(403);
            }
        }
}
