package org.project.controller.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.project.exception.ProductNotFoundException;
import org.project.model.Product;
import org.project.service.ProductService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/v1/products/*")
public class ProductApiServlet extends HttpServlet {

    private final ProductService productService;
    private final ObjectMapper mapper;

    public ProductApiServlet() {
        this.productService = new ProductService();
        this.mapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        String pathInfo = req.getPathInfo();
        PrintWriter writer = resp.getWriter();

        if (pathInfo == null || pathInfo.equals("/")) {
            getAllProducts(resp, writer);
        } else {
            try {
                Long productId = Long.valueOf(pathInfo.substring(1));
                getProductById(productId, resp, writer);
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writer.write("Wrong product id, should be a number");
            }
        }
    }

    private void getAllProducts(HttpServletResponse resp, PrintWriter writer) throws JsonProcessingException {
        List<Product> products = productService.findAll();
        String productsJson = mapper.writeValueAsString(products);
        resp.setStatus(HttpServletResponse.SC_OK);
        writer.write(productsJson);
    }

    private void getProductById(Long productId, HttpServletResponse resp, PrintWriter writer) throws JsonProcessingException {
        try {
            Product product = productService.findById(productId);
            String productJson = mapper.writeValueAsString(product);
            resp.setStatus(HttpServletResponse.SC_OK);
            writer.write(productJson);
        } catch (ProductNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            writer.write(String.format("Product with id %d not found", productId));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        String pathInfo = req.getPathInfo();
        PrintWriter writer = resp.getWriter();

        if (pathInfo != null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writer.write("Wrong path");
            return;
        }

        Product product = mapper.readValue(req.getInputStream(), Product.class);
        product = productService.save(product);
        String productJson = mapper.writeValueAsString(product);

        resp.setStatus(HttpServletResponse.SC_CREATED);
        writer.write(productJson);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        String pathInfo = req.getPathInfo();
        PrintWriter writer = resp.getWriter();

        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writer.write("Wrong path");
            return;
        }

        Product product = mapper.readValue(req.getInputStream(), Product.class);
        Long productId = null;

        try {
            productId = Long.valueOf(pathInfo.substring(1));
            product = productService.update(product, productId);
            String productJson = mapper.writeValueAsString(product);
            resp.setStatus(HttpServletResponse.SC_OK);
            writer.write(productJson);
        } catch (ProductNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            writer.write(String.format("Product with id %d not found", productId));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writer.write("Wrong path");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        PrintWriter writer = resp.getWriter();

        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writer.write("Wrong path");
            return;
        }

        try {
            Long productId = Long.valueOf(pathInfo.substring(1));
            productService.deleteById(productId);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writer.write("Wrong product id, should be a number");
        }
    }
}
