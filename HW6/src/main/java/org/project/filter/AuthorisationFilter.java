package org.project.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import org.project.util.JwtUtil;

import java.io.IOException;

@WebFilter("/*")
public class AuthorisationFilter extends HttpFilter {

    @Override
    public void doFilter(ServletRequest req,
                         ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {

        String requestURI = ((HttpServletRequest) req).getRequestURI();
        System.out.println(requestURI);
        System.out.println("----------" + isPublicResource(requestURI) + "----------");
        if (isPublicResource(requestURI)) {
            chain.doFilter(req, resp);
            return;
        }

        Cookie[] cookies = ((HttpServletRequest) req).getCookies();
        String token = extractTokenFromCookies(cookies);

        if (JwtUtil.validate(token)) {
            chain.doFilter(req, resp);
        } else {
            ((HttpServletResponse) resp).sendRedirect("/HW6/login");
        }
    }

    private String extractTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if ("jwt".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private boolean isPublicResource(String requestURI) {
        return     requestURI.equals("/HW6/signup")
                || requestURI.equals("/HW6/login")
                || requestURI.endsWith(".css")
                || requestURI.endsWith(".js")
                || requestURI.endsWith(".html");
    }
}
