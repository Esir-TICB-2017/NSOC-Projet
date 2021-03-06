package webserver;


import com.google.common.net.HttpHeaders;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

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
		Boolean isLogoutServlet = request.getRequestURI().equals("/logout");
		Boolean isIndex = request.getRequestURI().equals("/") || request.getRequestURI().equals("index.html");
		if (isStatic || isLoginServlet || isIndex || isLogoutServlet) {
			chain.doFilter(request, response);
			return;
		} else {
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals("Set-Cookie")) {
						String token = cookie.getValue();
						Boolean isAuthenticated = SessionManager.checkAuthentication(token);
						if (isAuthenticated) {
							chain.doFilter(request, response);
							return;
						} else {
							response.sendError(403);
							return;
						}
					}
				}
			}

		}
	}

	@Override
	public void destroy() {

	}
}