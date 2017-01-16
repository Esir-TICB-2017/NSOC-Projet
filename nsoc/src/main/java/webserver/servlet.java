package webserver;

/**
 * Created by Dann on 16/01/2017.
 */

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.printWriter;

public class servlet1 extends HttpServlet
{
    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException
    {
        response.setContentType("text/html");
        PrintWriter out=response.getWriter();
        out.println("Ta mere");
    }
}