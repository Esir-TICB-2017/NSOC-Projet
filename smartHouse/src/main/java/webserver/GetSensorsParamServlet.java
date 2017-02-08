package webserver;


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
public class GetSensorsParamServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject message =  new JSONObject();

        JSONArray arraySensor;
        JSONObject objSensor =  new JSONObject();

        JSONArray arrayIndicator;
        JSONObject objIndicator = new JSONObject();
        JSONObject data;

        for (Sensor sensor : Sensors.getInstance().getSensors()){
            arraySensor = new JSONArray();
            data = new JSONObject();
            data.put("name", sensor.getLastRecord().getName());
            data.put("data", sensor.getLastRecord().getData());
            data.put("date", sensor.getLastRecord().getDate());
            data.put("unit", sensor.getUnit());
            data.put("status", sensor.getStatus());
            arraySensor.put(data);
            objSensor.put(sensor.getType(),arraySensor);
        }
        message.put("Sensors", objSensor);

        for (Indicator indicator : Indicators.getInstance().getIndicators()){
            arrayIndicator = new JSONArray();
            data = new JSONObject();
            data.put("name", indicator.getLastRecord().getName());
            data.put("data", indicator.getLastRecord().getData());
            data.put("date", indicator.getLastRecord().getDate());
            arrayIndicator.put(data);
            objIndicator.put(indicator.getType(),arrayIndicator);
        }
        message.put("Indicateurs",objIndicator);

        String responseData = message.toString();
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(responseData);

    }
}
