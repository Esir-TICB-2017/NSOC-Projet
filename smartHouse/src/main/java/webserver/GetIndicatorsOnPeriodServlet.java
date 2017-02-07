package webserver;

import database.data.DataRecord;
import indicators.Indicator;
import indicators.Indicators;
import org.json.JSONArray;

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
		String indicatorType = request.getParameter("indicator");
		Indicator indicator = Indicators.getInstance().getIndicatorByString(indicatorType);
		ArrayList<DataRecord> results = indicator.getRecordsOnPeriod(startDate, endDate);

		JSONArray responseData = new JSONArray(results);
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println(responseData);

	}
}
