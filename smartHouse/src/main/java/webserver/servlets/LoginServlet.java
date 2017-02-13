package webserver.servlets;

import com.google.api.client.auth.openidconnect.IdToken;
import com.google.api.client.json.webtoken.JsonWebToken;
import database.ReadInDatabase;
import database.WriteInDatabase;
import webserver.SessionManager;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.ws.rs.core.NewCookie;
import java.io.*;
import java.net.HttpCookie;

/**
 * Created by loulou on 22/01/2017.
 */
public class LoginServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idTokenString = request.getParameter("idtoken");
		IdToken idToken = SessionManager.getGoogleIdToken(idTokenString);
		if (idToken != null) {
			String userId = idToken.getPayload().getSubject();
			String email = (String) idToken.getPayload().get("email");
			System.out.println(ReadInDatabase.checkExistingUser(email));
			if (ReadInDatabase.checkExistingUser(email)) {
				String token = SessionManager.createJWT("http://smarthouseapp.com", userId, 5 * 60 * 1000);
				WriteInDatabase.writeNewToken(email, token);
				Cookie cookie = new Cookie("Set-Cookie", token);
				cookie.setPath(";Path=/;HttpOnly;");
				response.addCookie(cookie);
				response.setStatus(HttpServletResponse.SC_OK);
			}
		} else {
			request.getRequestDispatcher("/login").include(request, response);
		}
	}
}
