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
        Indicator indicator = Indicators.getInstance().getIndicatorByString(request.getParameter("indicator"));
        result = ReadInDatabase.getIndicatorsOnPeriod(indicator, startDate, endDate);
       if(result != null) {
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
