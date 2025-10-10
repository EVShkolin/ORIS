package org.project.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.project.util.FileManager;
import org.project.util.ParamsValidator;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/home")
public class MyServlet extends HttpServlet {
    private final String HOME_PAGE = "/html/form.html";
    private final String SUCCESS_PAGE = "/html/success.html";

    private final ParamsValidator validator;
    private final FileManager fileManager;

    public MyServlet() {
        this.fileManager = new FileManager();
        this.validator = new ParamsValidator();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(HOME_PAGE);
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String errorParams = validator.getErrorParams(name, email);

        if (!errorParams.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/home" + errorParams);
            return;
        }

        fileManager.save(name, email);
        System.out.println(LocalDateTime.now() + "New user: " + name + " email: " + email);
        RequestDispatcher dispatcher = req.getRequestDispatcher(SUCCESS_PAGE);
        dispatcher.forward(req, resp);
    }
}
