package webserver.servlets;

import com.google.api.client.auth.openidconnect.IdToken;
import database.ReadInDatabase;
import webserver.SessionManager;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.*;

/**
 * Created by loulou on 22/01/2017.
 */
public class LoginServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idTokenString = request.getParameter("idtoken");
		IdToken idToken = SessionManager.getGoogleIdToken(idTokenString);
		if(idToken != null) {
			String userId = idToken.getPayload().getSubject();
			if(ReadInDatabase.checkExistingUser(userId)) {
				String sessionToken = SessionManager.createJWT("http://smarthouseapp.com", userId, 5 * 60 * 1000);
				response.getWriter().print(sessionToken);
				response.setStatus(HttpServletResponse.SC_OK);
			}
		} else {
			request.getRequestDispatcher("/login").include(request, response);
		}
	}
}
