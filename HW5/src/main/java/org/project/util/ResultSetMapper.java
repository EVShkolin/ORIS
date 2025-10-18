package org.project.util;

import org.project.model.Product;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetMapper {

    public static Product mapToProduct(ResultSet resultSet) throws SQLException {
        return new Product(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getInt("quantity"),
                new BigDecimal(resultSet.getString("price")),
                resultSet.getString("image_url")
        );
    }

}
