package webserver;

import com.google.gson.Gson;
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

public class GetIndicatorServlet extends HttpServlet{

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        DataLinkToDate result;
        String indicator = request.getParameter("indicator");
        switch (indicator) {
            case "global": {
                result = ReadInDatabase.getLastIndicator("globalInd");
                break;
            }
            case "temperature": {
                result = ReadInDatabase.getLastIndicator("temperatureInd");
                break;
            }
            case "co2": {
                result = ReadInDatabase.getLastIndicator("co2Ind");
                break;
            }
            case "consumption": {
                result = ReadInDatabase.getLastIndicator("consumptionInd");
                break;
            }
            case "humidity": {
                result = ReadInDatabase.getLastIndicator("humidityInd");
                break;
            }
            case "production": {
                result = ReadInDatabase.getLastIndicator("productionInd");
                break;
            }
            default:
                result = null;
        }

        if(result != null) {
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
