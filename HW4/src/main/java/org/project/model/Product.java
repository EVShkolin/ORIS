package org.project.model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 1;

    private Long id;

    private String name;

    private Integer quantity = 1;

    private BigDecimal price;

    private String imageUrl;

    public Product(Long id, String name, BigDecimal price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.quantity = 1;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
