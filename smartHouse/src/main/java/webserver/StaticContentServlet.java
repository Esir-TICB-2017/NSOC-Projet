package webserver;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StaticContentServlet extends HttpServlet
{
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        RequestDispatcher rd = getServletContext().getNamedDispatcher("default");
        System.out.println("ici");
        HttpServletRequest wrapped = new HttpServletRequestWrapper(req) {
            public String getServletPath() { return ""; }
        };

        rd.forward(wrapped, resp);
    }
}