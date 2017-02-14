package webserver.servlets;


import database.data.DataRecord;
import indicators.Indicator;
import indicators.Indicators;
import org.json.JSONArray;
import org.json.JSONObject;
import sensor.sensorClass.Sensor;
import sensor.sensorClass.Sensors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by clement on 08/02/2017.
 */
public class GetFirstDataServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JSONArray globalResults = new JSONArray();
        JSONObject data;

        for (Sensor sensor : Sensors.getInstance().getSensors()){
            DataRecord lastRecord = sensor.getLastRecord();
            data = lastRecord.toJson();
            data.put("unit", sensor.getUnit());
            data.put("connected", sensor.getStatus());
            globalResults.put(data);
        }

        for (Indicator indicator : Indicators.getInstance().getIndicators()){
            DataRecord lastRecord = indicator.getLastRecord();
            data = lastRecord.toJson();
            globalResults.put(data);
        }

        String responseData = globalResults.toString();
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(responseData);
    }
}
