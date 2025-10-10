package org.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.project.model.Product;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/cart")
public class CartServlet extends HttpServlet {

    private final ObjectMapper mapper;

    public CartServlet() {
        this.mapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Map<Long, Product> cart = (Map<Long, Product>) session.getAttribute("cart");

        if (cart == null) {
            cart = new HashMap<>();
        }

        List<Product> products = new ArrayList<>(cart.values());
        String productsJson = mapper.writeValueAsString(products);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter writer = resp.getWriter();
        writer.print(productsJson);
        writer.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Product product = mapper.readValue(req.getInputStream(), Product.class);

        HttpSession session = req.getSession();
        Map<Long, Product> cart = (Map<Long, Product>) session.getAttribute("cart");

        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }
        if (cart.containsKey(product.getId())) {
            Product existingProduct = cart.get(product.getId());
            existingProduct.setQuantity(existingProduct.getQuantity() + 1);
        } else {
            cart.put(product.getId(), product);
        }
    }
}
