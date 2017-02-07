package webserver;

import database.WriteInDatabase;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		SessionManager.deleteSession(req);
		response.setStatus(HttpServletResponse.SC_OK);
		return;
	}
}