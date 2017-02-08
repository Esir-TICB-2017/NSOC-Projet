package webserver;

import com.google.gson.Gson;
import database.data.DataRecord;
import sensor.sensorClass.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class GetValuesOnPeriodServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long start = Long.parseLong(request.getParameter("startDate"));
        Long end = Long.parseLong(request.getParameter("endDate"));
        Timestamp startDate = new Timestamp(start * 1000);
        Timestamp endDate = new Timestamp(end * 1000);
        String sensorName = request.getParameter("objectName");
        Sensor sensor = Sensors.getInstance().getSensorByString(sensorName);
		System.out.println(startDate.toString());
		System.out.println(endDate.toString());
		ArrayList<DataRecord> results = sensor.getRecordsOnPeriod(startDate, endDate);

		Gson gson = new Gson();
		String responseData = gson.toJson(results);
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println(responseData);

    }
}