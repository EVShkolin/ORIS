package org.project.controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.project.dto.FormDataDto;
import org.project.service.UserService;
import org.project.util.JwtUtil;

import java.io.IOException;

@WebServlet("/signup")
public class SignupPageServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        this.userService = (UserService) context.getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("signup.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        FormDataDto formDataDto = new FormDataDto(username, password);

        userService.save(formDataDto);
        String token = JwtUtil.generateToken(username);
        Cookie cookie = new Cookie("jwt", token);
        cookie.setMaxAge(10 * 60);
        resp.addCookie(cookie);
        resp.sendRedirect("/HW6/home");
    }
}
