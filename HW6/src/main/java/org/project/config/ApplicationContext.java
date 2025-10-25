package org.project.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.project.util.UserMapper;
import org.project.repository.UserRepository;
import org.project.service.UserService;
import org.project.util.BCryptPasswordEncoder;

import javax.sql.DataSource;

@WebListener
public class ApplicationContext implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        UserRepository userRepository = new UserRepository(getDataSource());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        UserMapper mapper = new UserMapper();

        UserService userService = new UserService(userRepository, mapper, encoder);
        context.setAttribute("userService", userService);
    }

    private DataSource getDataSource() {
        String username = System.getenv("POSTGRES_USERNAME");
        String password = System.getenv("POSTGRES_PASSWORD");

        HikariDataSource ds = new HikariDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setJdbcUrl("jdbc:postgresql://host.docker.internal:5432/users");
        ds.setUsername(username);
        ds.setPassword(password);

        return ds;
    }
}
