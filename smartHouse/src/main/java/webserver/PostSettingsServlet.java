package webserver;

import com.google.gson.Gson;
import database.WriteInDatabase;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Quentin on 31/1/17.
 */

public class PostSettingsServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String stringRequest = request.getQueryString();

        JSONObject jObject = new JSONObject(stringRequest);
        JSONArray result = jObject.getJSONArray("users_settings");

        //retrieve userid
        String userid = (String) request.getSession().getAttribute("userId");

        //write settings in database
        WriteInDatabase.writeUserSettings(userid ,result);


        if (result != null) {
            Gson gson = new Gson();
            String json = gson.toJson(result);
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(json);
        } else {
            response.sendError(403);
        }
    }
}
