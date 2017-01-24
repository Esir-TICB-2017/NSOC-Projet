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
        System.out.println(request.getRequestURI());
        String path = request.getRequestURI().substring(request.getContextPath().length());
//
        if (path.startsWith("/static")) {
            chain.doFilter(request, servletResponse); // Goes to default servlet.
            return;
        }
        if(request.getRequestURI().equals("/login.html")){
            chain.doFilter(servletRequest, servletResponse);
        }
        else {
            HttpSession session = request.getSession(false);
            if(session!=null){
                String name=(String)session.getAttribute("name");
                chain.doFilter(servletRequest, servletResponse);
                System.out.println("Hello, "+name+" Welcome to Profile");

            }
            else{
                System.out.println("Please login first");
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                response.sendRedirect("/login.html");
            }
        }

    }
    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }
    @Override
    public void destroy() {}
}