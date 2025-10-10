package org.project.service;

import org.project.dao.ProductDao;
import org.project.model.Product;

import java.util.List;

public class ProductService {

    private final ProductDao productDao;

    public ProductService() {
        this.productDao = new ProductDao();
    }

    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }

    public void save(List<Product> products) {
        productDao.save(products);
    }
}
