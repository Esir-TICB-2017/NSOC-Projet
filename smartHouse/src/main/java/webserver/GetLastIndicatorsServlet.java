package webserver;

import com.google.gson.Gson;
import database.ReadInDatabase;
import database.data.DataLinkToDate;
import indicators.Indicator;
import indicators.Indicators;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Quentin on 30/1/17.
 */
public class GetLastIndicatorsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DataLinkToDate result;
        String type = request.getParameter("indicator");
        Indicator indicator = Indicators.getInstance().getIndicatorByString(type);
        result = ReadInDatabase.getLastIndicator(indicator);

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