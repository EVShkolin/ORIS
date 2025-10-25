package org.project.repository;

import lombok.RequiredArgsConstructor;
import org.project.model.User;
import org.project.util.ResultSetConverter;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

@RequiredArgsConstructor
public class UserRepository {

    private final DataSource dataSource;

    public Optional<User> findByUsername(String username) {
        String query = "SELECT * FROM users WHERE name = ?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, username);

            ResultSet resultSet = ps.executeQuery();
            return resultSet.next() ?
                    Optional.of(ResultSetConverter.convertToUser(resultSet)) : Optional.empty();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    public User save(User user) {
        String query = "INSERT INTO users (name, password_hash) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            generatedKeys.next();
            user.setId(generatedKeys.getLong("id"));
            return user;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }

}
