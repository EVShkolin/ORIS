package org.project.util;

import org.project.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetConverter {

    public static User convertToUser(ResultSet resultSet) throws SQLException {
        return new User(resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("password_hash"));
    }
}
