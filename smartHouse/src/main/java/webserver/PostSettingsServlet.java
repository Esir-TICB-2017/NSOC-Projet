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

public class PostSettingsServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ArrayList<String> result=null;

        //retrieve userid
        String userid = (String) request.getSession().getAttribute("userId");

        //build result Arraylist
        //step settings
        result.set(0, request.getParameter("settemp"));     //setTempStep
        result.set(1, request.getParameter("setco2min"));   //setCo2MinStep
        result.set(2, request.getParameter("setco2max"));   //setCo2MaxStep
        result.set(3, request.getParameter("setconsobj"));  //setConsObj
        result.set(4, request.getParameter("setprodobj"));  //setProdObj

        //home page settings
        result.set(5, request.getParameter("tempind"));         //displayTemperatureIndOnHome
        result.set(6, request.getParameter("humidityind"));     //displayHumidityIndOnHome
        result.set(7, request.getParameter("co2ind"));          //displayCo2IndOnHome
        result.set(8, request.getParameter("consind"));         //displayConsumptionIndOnHome
        result.set(9, request.getParameter("prodind"));         //displayProductionIndOnHome
        result.set(10, request.getParameter("perhome"));        //periodChartOnHome

        //data page settings
        result.set(11, request.getParameter("globalchart"));    //displayGlobalChartOnData
        result.set(12, request.getParameter("tempchart"));      //displayTemperatureChartOnData
        result.set(13, request.getParameter("humiditychart"));  //displayHumidityChartOnData
        result.set(14, request.getParameter("co2chart"));       //displayCo2ChartOnData
        result.set(15, request.getParameter("conschart"));      //displayConsumptionChartOnData
        result.set(16, request.getParameter("prodchart"));      //displayProductionChartOnData
        result.set(17, request.getParameter("perdata"));        //periodChartsOnData

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
