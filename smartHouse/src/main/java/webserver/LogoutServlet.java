package webserver;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;


public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = req.getSession();
        session.invalidate();
        System.out.println("User Logout");
        response.sendRedirect("/login.html");
        return;
    }
}