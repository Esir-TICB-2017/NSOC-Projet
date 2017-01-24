package webserver;

import database.ReadInDatabase;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public class RequestFilter implements Filter {
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = request.getRequestURI().substring(request.getContextPath().length());
        Boolean isStatic = path.startsWith("/static");
        Boolean isLoginPage = request.getRequestURI().equals("/login.html");
        Boolean isAuthenticated = checkAuthentication(request, response);
        if(isStatic || isLoginPage || isAuthenticated) {
            chain.doFilter(request, response);
        }
        else {
            redirectOnLogin(request, response);
        }

    }
    public void redirectOnLogin(HttpServletRequest request , HttpServletResponse response){
        System.out.println("Redirect to Login page");
        try {
            response.sendRedirect("/login.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    public Boolean checkAuthentication(HttpServletRequest request , HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if(session != null) {
        String userId = (String) session.getAttribute("userId");
        String userToken = (String) session.getAttribute("userToken");
            try {
                HashMap userSession = ReadInDatabase.getUserSession(userId).get(0);
                if (userSession.get("token").equals(userToken) && userSession.get("userid").equals(userId)) {
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


    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }
    @Override
    public void destroy() {}
}