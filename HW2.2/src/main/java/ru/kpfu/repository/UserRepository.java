package ru.kpfu.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.kpfu.model.User;
import ru.kpfu.util.ResultSetConverter;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final DataSource dataSource;

    public List<User> findAll() {
        String query = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                User user = ResultSetConverter.convertToUser(rs);
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return users;
    }

    public User findById(Long id) {
        String query = "SELECT * FROM users WHERE id = ?";
        User user = null;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            user = ResultSetConverter.convertToUser(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return user;
    }

    public User save(User user) {
        return user.getId() == null ? insert(user) : update(user);
    }

    private User insert(User user) {
        String query = """
                INSERT INTO users (name, age)
                VALUES (?, ?)
                """;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getName());
            ps.setInt(2, user.getAge());
            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            generatedKeys.next();
            user.setId(generatedKeys.getLong("id"));
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return user;
    }

    private User update(User user) {
        String query = "UPDATE users SET name = ?, age = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, user.getName());
            ps.setInt(2, user.getAge());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return user;
    }

    public void deleteById(Long id) {
        String query = "DELETE FROM users WHERE id = ?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
