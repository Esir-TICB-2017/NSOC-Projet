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

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by loulou on 22/01/2017.
 */
public class SigninTokenServlet extends HttpServlet{
    String clientId = "299325628592-hqru0vumh16bp0hhhvj9qr35lglm8gqu.apps.googleusercontent.com";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();

        String idTokenString = request.getParameter("idtoken");
        JsonFactory jsonFactory = new JacksonFactory();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(clientId))
                .build();

// (Receive idTokenString by HTTPS POST)

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
            System.out.println("User ID: " + userId);

            // Get profile information from payload
           // String email = payload.get
            //boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");




            ArrayList<HashMap> userInfo = ReadInDatabase.getUser(userId);

            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            Cookie cookie = new Cookie("userId", userId);
            response.addCookie(cookie);
            //response.sendRedirect(request.getRequestURL().toString());


        } else {
            System.out.println("Invalid ID token.");
        }
        } catch (Exception e ){
            e.printStackTrace();
        }


//        JSONObject obj = new JSONObject(result);
//        Gson gson = new Gson();
//        String json = gson.toJson(result);
//
//        response.setContentType("text/html");
//        response.setStatus(HttpServletResponse.SC_OK);
//        response.getWriter().println(json);
    }
}
