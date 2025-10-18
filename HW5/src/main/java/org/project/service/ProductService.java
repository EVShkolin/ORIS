package org.project.service;

import lombok.extern.slf4j.Slf4j;
import org.project.exception.ProductNotFoundException;
import org.project.model.Product;
import org.project.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService() {
        this.productRepository = new ProductRepository();
    }

    public List<Product> findAll() {
        log.debug("IN ProductService find all products");
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        log.debug("IN ProductService find by id {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

    }

    public Product save(Product product) {
        log.debug("IN ProductService product save product {}", product);
        return productRepository.save(product);
    }

    public Product update(Product updatedProduct, Long id) {
        log.debug("IN ProductService update product with id {}", id);
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setQuantity(updatedProduct.getQuantity());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setImageUrl(updatedProduct.getImageUrl());

        return productRepository.save(existingProduct);
    }

    public void deleteById(Long id) {
        log.info("IN ProductService delete product with id {}", id);
        productRepository.deleteById(id);
    }

    public List<Product> getProductsFromCart(Map<Long, Integer> cart) {
        List<Product> products = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Product product = productRepository.findById(entry.getKey())
                    .orElseThrow(() -> new ProductNotFoundException(entry.getKey()));

            products.add(product);
        }

        return products;
    }
}
