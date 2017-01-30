package webserver;

import com.google.gson.Gson;
import computeAggregatedData.Indicators;
import database.ReadInDatabase;
import database.data.DataLinkToDate;
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
        String indicator = request.getParameter("indicator");
        switch (indicator) {
            case "global": {
                result = ReadInDatabase.getLastIndicator(Indicators.GLOBAL);
                break;
            }
            case "temperature": {
                result = ReadInDatabase.getLastIndicator(Indicators.TEMPERATURE);
                break;
            }
            case "co2": {
                result = ReadInDatabase.getLastIndicator(Indicators.CO2);
                break;
            }
            case "consumption": {
                result = ReadInDatabase.getLastIndicator(Indicators.CONSUMPTION);
                break;
            }
            case "humidity": {
                result = ReadInDatabase.getLastIndicator(Indicators.HUMIDITY);
                break;
            }
            case "production": {
                result = ReadInDatabase.getLastIndicator(Indicators.PRODUCTION);
                break;
            }
            default:
                result = null;
        }

        if (result != null) {
            JSONObject obj = new JSONObject(result);
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