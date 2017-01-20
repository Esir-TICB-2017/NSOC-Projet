package webserver;

import sensor.sensorClass.ConsumptionSensor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GetLastValueServlet extends HttpServlet
{
    private ConsumptionSensor sensor;
    public GetLastValueServlet(ConsumptionSensor sensor) {
        this.sensor = sensor;
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(sensor.getLastValue());
        response.getWriter().println("session=" + request.getSession(true).getId());
    }
}