package webserver;

import database.ReadInDatabase;
import database.WriteInDatabase;
import utils.SessionIdentifierGenerator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Created by loulou on 25/01/2017.
 */
public class SessionManager {

    public static Boolean checkAuthentication(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String userId = (String) session.getAttribute("userId");
            String userToken = (String) session.getAttribute("userToken");
            java.util.Date today = new java.util.Date(System.currentTimeMillis());
            Timestamp currentDate = new Timestamp(today.getTime());
            HashMap userSession = ReadInDatabase.getUserSession(userId).get(0);
            Boolean isSameToken = userSession.get("token").equals(userToken);
            Boolean isSameUser = userSession.get("userid").equals(userId);
            Timestamp expirationDate = (Timestamp) userSession.get("expiration_date");
            Boolean validExpirationDate = expirationDate.after(currentDate);
            if (isSameToken && isSameUser && validExpirationDate) {
                return true;
            } else {
                return false;
            }

        }
        return false;
    }

    public static HttpServletRequest createSession(String userId, String name, HttpServletRequest request) {
        java.util.Date today = new java.util.Date(System.currentTimeMillis() + 5 * 60 * 1000);
        Timestamp expirationDate = new Timestamp(today.getTime());
        SessionIdentifierGenerator sig = new SessionIdentifierGenerator();
        String sessionToken = sig.nextSessionId();
        WriteInDatabase.storeNewSession(sessionToken, userId, name, expirationDate);
        HttpSession session = request.getSession();
        session.setAttribute("userToken", sessionToken);
        session.setAttribute("name", sessionToken);
        session.setAttribute("userId", userId);
        return request;

    }

    public static void deleteSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        WriteInDatabase.deleteUserSession((String) session.getAttribute("userId"));
        session.invalidate();
    }

}

