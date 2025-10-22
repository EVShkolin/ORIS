package org.project.repository;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.project.model.Product;
import org.project.util.ResultSetMapper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ProductRepository {

    private final DataSource dataSource;

    public ProductRepository() {
        this.dataSource = getDataSource();
    }

    public List<Product> findAll() {
        String query = "SELECT * FROM products";
        List<Product> products = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            ResultSet resultSet = connection.createStatement().executeQuery(query);
            while (resultSet.next()) {
                Product product = ResultSetMapper.mapToProduct(resultSet);
                products.add(product);
            }
        } catch (SQLException e) {
            log.warn(e.getSQLState());
        }

        return products;
    }

    public Optional<Product> findById(Long id) {
        String query = "SELECT * FROM products WHERE id = ?";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, id);
            ResultSet resultSet = ps.executeQuery();

            return resultSet.next() ? Optional.of(ResultSetMapper.mapToProduct(resultSet)) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Product save(Product product) {
        if (product.getId() == null) {
            return insert(product);
        } else {
            return update(product);
        }
    }

    private Product insert(Product product) {
        String query = "INSERT INTO products (name, quantity, price, image_url) VALUES (?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, product.getName());
            ps.setInt(2, product.getQuantity());
            ps.setBigDecimal(3, product.getPrice());
            ps.setString(4, product.getImageUrl());
            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            generatedKeys.next();
            Long id = generatedKeys.getLong("id");
            product.setId(id);

            return product;
        } catch (SQLException e) {
            log.warn(e.getSQLState());
            throw new RuntimeException("Could not save product " + product);
        }
    }

    private Product update(Product product) {
        String query = "UPDATE products SET name = ?, quantity = ?, price = ?, image_url = ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, product.getName());
            ps.setInt(2, product.getQuantity());
            ps.setBigDecimal(3, product.getPrice());
            ps.setString(4, product.getImageUrl());
            ps.setLong(5, product.getId());
            ps.executeUpdate();

            return product;
        } catch (SQLException e) {
            log.warn(e.getSQLState());
            throw new RuntimeException("Could not save product " + product);
        }
    }

    public void deleteById(Long id) {
        String query = "DELETE FROM products WHERE id = ?";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.warn(e.getSQLState());
            throw new RuntimeException("Could not delete product with id " + id);
        }
    }

    private static DataSource getDataSource() {
        String username = System.getenv("POSTGRES_USERNAME");
        String password = System.getenv("POSTGRES_PASSWORD");

        HikariDataSource ds = new HikariDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setJdbcUrl("jdbc:postgresql://host.docker.internal:5432/products");
        ds.setUsername(username);
        ds.setPassword(password);

        return ds;
    }
}
