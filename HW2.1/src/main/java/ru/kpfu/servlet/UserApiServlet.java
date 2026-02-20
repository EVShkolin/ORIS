package ru.kpfu.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.kpfu.model.User;
import ru.kpfu.service.UserService;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/v1/users/*")
public class UserApiServlet extends HttpServlet {

    private UserService userService;
    private ObjectMapper mapper;

    @Override
    public void init() throws ServletException {
        ApplicationContext context =
                    (AnnotationConfigApplicationContext) getServletContext().getAttribute("springContext");
        this.userService = context.getBean(UserService.class);
        this.mapper = context.getBean(ObjectMapper.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            List<User> users = userService.findAll();
            resp.getWriter().write(mapper.writeValueAsString(users));
            return;
        }

        Long id = Long.valueOf(path.substring(1));
        User user = userService.findById(id);
        resp.getWriter().write(mapper.writeValueAsString(user));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = mapper.readValue(req.getInputStream(), User.class);
        user = userService.save(user);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write(mapper.writeValueAsString(user));

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = mapper.readValue(req.getInputStream(), User.class);
        Long id = Long.valueOf(req.getPathInfo().substring(1));
        user = userService.update(user, id);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(mapper.writeValueAsString(user));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.valueOf(req.getPathInfo().substring(1));
        userService.deleteById(id);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
