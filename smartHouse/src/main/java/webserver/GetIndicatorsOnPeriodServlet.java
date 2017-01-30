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
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by Quentin on 30/1/17.
 */
public class GetIndicatorsOnPeriodServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long start = Long.parseLong(request.getParameter("startDate"));
        Long end = Long.parseLong(request.getParameter("endDate"));
        Timestamp startDate = new Timestamp(start * 1000);
        Timestamp endDate = new Timestamp(end * 1000);

        ArrayList<DataLinkToDate> result;
        String indicator = request.getParameter("indicator");
        switch (indicator) {
            case "global": {
                result = ReadInDatabase.getIndicatorsOnPeriod(Indicators.GLOBAL, startDate, endDate);
                break;
            }
            case "temperature": {
                result = ReadInDatabase.getIndicatorsOnPeriod(Indicators.TEMPERATURE, startDate, endDate);
                break;
            }
            case "co2": {
                result = ReadInDatabase.getIndicatorsOnPeriod(Indicators.CO2, startDate, endDate);
                break;
            }
            case "consumption": {
                result = ReadInDatabase.getIndicatorsOnPeriod(Indicators.CONSUMPTION, startDate, endDate);
                break;
            }
            case "humidity": {
                result = ReadInDatabase.getIndicatorsOnPeriod(Indicators.HUMIDITY, startDate, endDate);
                break;
            }
            case "production": {
                result = ReadInDatabase.getIndicatorsOnPeriod(Indicators.PRODUCTION, startDate, endDate);
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
