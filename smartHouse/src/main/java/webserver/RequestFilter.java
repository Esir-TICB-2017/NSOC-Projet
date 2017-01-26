package webserver;

import com.sun.org.apache.xpath.internal.SourceTree;

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
        String path = request.getRequestURI().substring(request.getContextPath().length());
        Boolean isStatic = path.startsWith("/static");
        Boolean isLoginServlet = request.getRequestURI().equals("/login");
        Boolean isIndex = request.getRequestURI().equals("/") || request.getRequestURI().equals("index.html");
       if (isStatic || isLoginServlet || isIndex) {
            chain.doFilter(request, response);
        } else {
            Boolean isAuthenticated = SessionManager.checkAuthentication(request);
            if(isAuthenticated) {
                chain.doFilter(request, response);
            }
            else {
                response.sendError(403);
            }
        }

    }

    @Override
    public void destroy() {

    }
}