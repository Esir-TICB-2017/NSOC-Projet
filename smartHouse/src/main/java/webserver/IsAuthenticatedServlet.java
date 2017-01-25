package webserver;

import database.ReadInDatabase;
import database.WriteInDatabase;
import org.apache.http.protocol.HTTP;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Created by loulou on 25/01/2017.
 */
public class IsAuthenticatedServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(checkAuthentication(request, response)){
            response.getWriter().write("true");
        } else {
            response.getWriter().write("false");
        }
        return;
    }

    public Boolean checkAuthentication(HttpServletRequest request , HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if(session != null) {
            String userId = (String) session.getAttribute("userId");
            String userToken = (String) session.getAttribute("userToken");
            java.util.Date today = new java.util.Date(System.currentTimeMillis());
            Timestamp currentDate = new Timestamp(today.getTime());
            try {
                HashMap userSession = ReadInDatabase.getUserSession(userId).get(0);
                Boolean isSameToken = userSession.get("token").equals(userToken);
                Boolean isSameUser = userSession.get("userid").equals(userId);
                Timestamp expirationDate = (Timestamp)userSession.get("expiration_date");
                Boolean validExpirationDate = expirationDate.after(currentDate);
                if (isSameToken && isSameUser && validExpirationDate) {
                    return true;
                } else {
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
