package webserver;

import com.google.gson.Gson;
import database.data.DataLinkToDate;
import org.json.JSONObject;
import sensor.sensorClass.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class GetValuesOnPeriodServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long start = Long.parseLong(request.getParameter("startDate"));
        Long end = Long.parseLong(request.getParameter("endDate"));
        Timestamp startDate = new Timestamp(start * 1000);
        Timestamp endDate = new Timestamp(end * 1000);

        ArrayList<DataLinkToDate> result ;
        String sensorName = request.getParameter("sensorName");
        switch (sensorName) {
            case "temperature" : {
                result = TemperatureSensor.getInstance().getValuesOnPeriod(startDate, endDate);
                break;
            }
            case "co2": {
                result = CO2Sensor.getInstance().getValuesOnPeriod(startDate, endDate);
                break;
            }
            case "consumption" : {
                result = ConsumptionSensor.getInstance().getValuesOnPeriod(startDate, endDate);
                break;
            }
            case "humidity" : {
                result = HumiditySensor.getInstance().getValuesOnPeriod(startDate, endDate);
                break;
            }
            case "production" : {
                result = ProductionSensor.getInstance().getValuesOnPeriod(startDate, endDate);
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