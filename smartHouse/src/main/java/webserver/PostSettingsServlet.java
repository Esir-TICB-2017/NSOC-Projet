package webserver;

import com.google.gson.Gson;
import database.WriteInDatabase;

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
public class PostSettingsServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ArrayList<String> result=null;

        //retrieve userid
        String userid = request.getParameter("userid");

        //retrieve sensor steps settings
        String setTempStep = request.getParameter("settemp");
        String setCo2MinStep = request.getParameter("setco2min");
        String setCo2MaxStep = request.getParameter("setco2max");
        String setConsObj = request.getParameter("setconsobj");
        String setProdObj = request.getParameter("setprodobj");

        //retrieve home page settings
        String displayTemperatureIndOnHome = request.getParameter("tempind");
        String displayHumidityIndOnHome = request.getParameter("humidityind");
        String displayCo2IndOnHome = request.getParameter("co2ind");
        String displayConsumptionIndOnHome = request.getParameter("consind");
        String displayProductionIndOnHome = request.getParameter("prodind");
        String periodChartOnHome = request.getParameter("perhome");

        //retrieve data page settings
        String displayGlobalChartOnData = request.getParameter("globalchart");
        String displayTemperatureChartOnData = request.getParameter("tempchart");
        String displayHumidityChartOnData = request.getParameter("humiditychart");
        String displayCo2ChartOnData = request.getParameter("co2chart");
        String displayConsumptionChartOnData = request.getParameter("conschart");
        String displayProductionChartOnData = request.getParameter("prodchart");
        String periodChartsOnData = request.getParameter("perdata");


        //build result Arraylist
        result.set(0, setTempStep);
        result.set(1, setCo2MinStep);
        result.set(2, setCo2MaxStep);
        result.set(3, setConsObj);
        result.set(4, setProdObj);

        result.set(5, displayTemperatureIndOnHome);
        result.set(6, displayHumidityIndOnHome);
        result.set(7, displayCo2IndOnHome);
        result.set(8, displayConsumptionIndOnHome);
        result.set(9, displayProductionIndOnHome);
        result.set(10, periodChartOnHome);

        result.set(11, displayGlobalChartOnData);
        result.set(12, displayTemperatureChartOnData);
        result.set(13, displayHumidityChartOnData);
        result.set(14, displayCo2ChartOnData);
        result.set(15, displayConsumptionChartOnData);
        result.set(16, displayProductionChartOnData);
        result.set(17, periodChartsOnData);


        //write settings in database
        WriteInDatabase.writeSettings(userid ,result);


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
