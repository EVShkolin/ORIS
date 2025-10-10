package org.project.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.project.model.Product;
import org.project.service.ProductService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/order")
public class OrderServlet extends HttpServlet {

    private final ProductService productService;

    public OrderServlet() {
        this.productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Map<Long, Product> cart = (Map<Long, Product>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }
        List<Product> products = new ArrayList<>(cart.values());
        productService.save(products);

        session.removeAttribute("cart");
        resp.setStatus(200);
        resp.getWriter().write("Order created");
    }
}
