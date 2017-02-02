package webserver;

import com.google.gson.Gson;
import computeAggregatedData.Indicators;
import database.ReadInDatabase;
import database.WriteInDatabase;
import database.data.DataLinkToDate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Quentin on 31/1/17.
 */

/////////////////////TO DO
public class GetSettingsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ArrayList<String> result=null;
        String userid = request.getParameter("userid");

        result = ReadInDatabase.getSettings(userid);

        if (result != null) {
            Gson gson = new Gson();
            String json = gson.toJson(result);
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(json);
        } else {
            response.sendError(403);
        }
    }
}