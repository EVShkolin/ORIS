package ru.kpfu.util;

import ru.kpfu.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetConverter {

    public static User convertToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getInt("age")
        );
    }

}
