package webserver;

import com.google.api.client.auth.openidconnect.IdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import database.ReadInDatabase;
import database.WriteInDatabase;
import utils.SessionIdentifierGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.*;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by loulou on 22/01/2017.
 */
public class LoginServlet extends HttpServlet {
	//    TODO : hide clientId in a better place
	String clientId = "299325628592-hqru0vumh16bp0hhhvj9qr35lglm8gqu.apps.googleusercontent.com";

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
			JsonFactory jsonFactory = new JacksonFactory();

			String idTokenString = request.getParameter("idtoken");

			GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
					.setAudience(Collections.singletonList(clientId))
					.build();

			GoogleIdToken idToken = null;
			try {
				idToken = verifier.verify(idTokenString);
			} catch (GeneralSecurityException e) {
				e.printStackTrace();
			}
			if (idToken != null) {
				IdToken.Payload payload = idToken.getPayload();

				// Print user identifier
				String userId = payload.getSubject();

				SessionManager.createSession(userId, request);
				response.setStatus(HttpServletResponse.SC_OK);

			} else {
				System.out.println("Invalid ID token.");
				request.getRequestDispatcher("/login").include(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
