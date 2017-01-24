package webserver;

import database.ReadInDatabase;
import database.WriteInDatabase;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = req.getSession();
        session.invalidate();
        System.out.println("ici logout");
        response.sendRedirect("/");
        return;
    }
}