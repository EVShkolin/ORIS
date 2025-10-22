package org.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 1;

    private Long id;

    private String name;

    private Integer quantity;

    private BigDecimal price;

    private String imageUrl;

}
