package webserver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SigninServlet extends HttpServlet {
    String clientId = "299325628592-hqru0vumh16bp0hhhvj9qr35lglm8gqu.apps.googleusercontent.com";
    String clientSecret = "ejQF1ZgyRgHIrExGZ3Yvj0e7";
    private final String callbackUri = "http://localhost:8080/callback";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,IOException {

        // redirect to google for authorization
        StringBuilder oauthUrl = new StringBuilder().append("https://accounts.google.com/o/oauth2/auth")
                .append("?client_id=").append(clientId) // the client id from the api console registration
                .append("&response_type=code")
                .append("&scope=openid%20email") // scope is the api permissions we are requesting
                .append("&redirect_uri=").append(callbackUri) // the servlet that google redirects to after authorization
                .append("&state=this_can_be_anything_to_help_correlate_the_response%3Dlike_session_id")
                .append("&access_type=offline") // here we are asking to access to user's data while they are not signed in
                .append("&approval_prompt=force"); // this requires them to verify which account to use, if they are already signed in

        resp.sendRedirect(oauthUrl.toString());
    }
}