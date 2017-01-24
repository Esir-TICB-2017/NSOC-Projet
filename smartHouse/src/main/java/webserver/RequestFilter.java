package webserver;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RequestFilter implements Filter {
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession(false);
        if(session!=null){

            String name=(String)session.getAttribute("name");
            chain.doFilter(servletRequest, servletResponse);
            System.out.println("Hello, "+name+" Welcome to Profile");

        }
        else{
            System.out.println("Please login first");
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.sendRedirect("/");
        }
    }
    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }
    @Override
    public void destroy() {}
}