package webserver;

import com.google.gson.Gson;
import org.json.JSONObject;
import sensor.sensorClass.ConsumptionSensor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class GetValuesOnPeriodServlet extends HttpServlet
{
    private ConsumptionSensor sensor;
    public GetValuesOnPeriodServlet(ConsumptionSensor sensor) {
        this.sensor = sensor;
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        Long start = Long.parseLong(request.getParameter("startDate"));
        Long end = Long.parseLong(request.getParameter("endDate"));
        Timestamp startDate = new Timestamp(start * 1000);
        Timestamp endDate = new Timestamp(end * 1000);
        ArrayList<HashMap> result = sensor.getValuesOnPeriod(sensor, startDate, endDate);

        JSONObject obj = new JSONObject(result);
        Gson gson = new Gson();
        String json = gson.toJson(result);

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(json);
    }
}