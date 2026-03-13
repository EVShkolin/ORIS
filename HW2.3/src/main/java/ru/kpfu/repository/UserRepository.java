package ru.kpfu.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.kpfu.model.User;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final NamedParameterJdbcTemplate namedTemplate;

    private final RowMapper<User> userMapper = (rs, rowNumber) ->
        new User(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getInt("age")
        );

    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return namedTemplate.query(sql, userMapper);
    }

    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = :id";
        SqlParameterSource params = new MapSqlParameterSource("id", id);

        try {
            return Optional.ofNullable(namedTemplate.queryForObject(sql, params, userMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public User save(User user) {
        return user.getId() == null ? insert(user) : update(user);
    }

    private User insert(User user) {
        String sql = """
                INSERT INTO users (name, age)
                VALUES (:name, :age)
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", user.getName())
                .addValue("age", user.getAge());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedTemplate.update(sql, params, keyHolder);
        Number generatedId = keyHolder.getKey();

        if (generatedId != null) {
            user.setId(generatedId.longValue());
        }

        return user;
    }

    private User update(User user) {
        String sql = "UPDATE users SET name = :name, age = :age WHERE id = :id";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", user.getName())
                .addValue("age", user.getAge())
                .addValue("id", user.getId());
        namedTemplate.update(sql, params);
        return user;
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM users WHERE id = :id";
        SqlParameterSource params = new MapSqlParameterSource("id", id);

        namedTemplate.update(sql, params);
    }

}
