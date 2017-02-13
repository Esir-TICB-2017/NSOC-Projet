package webserver.servlets;

import database.WriteInDatabase;
import webserver.SessionManager;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("Set-Cookie")) {
				cookie.setMaxAge(-1);
				response.addCookie(cookie);
			}
		}
		response.setStatus(HttpServletResponse.SC_OK);
		return;
	}
}