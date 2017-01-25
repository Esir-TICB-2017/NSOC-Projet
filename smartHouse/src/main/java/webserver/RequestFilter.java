package webserver;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RequestFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        System.out.println(request.getRequestURI());
        String path = request.getRequestURI().substring(request.getContextPath().length());
        Boolean isJs = path.startsWith("/static/js");
        Boolean isLoginPage = request.getRequestURI().equals("/login.html");
        Boolean isIndex = request.getRequestURI().equals("/index.html");

        chain.doFilter(request, response);
        /*
        if (isLoginPage || isJs || isIndex) {
            chain.doFilter(request, response);
        } else {
            Boolean isAuthenticated = SessionManager.checkAuthentication(request);
            if(isAuthenticated) {
                chain.doFilter(request, response);
            }
            else {
                response.sendError(403);
            }
        }*/

    }

    @Override
    public void destroy() {

    }
}