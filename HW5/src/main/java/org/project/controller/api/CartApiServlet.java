package org.project.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.project.dto.CartItemDto;
import org.project.model.Product;
import org.project.service.ProductService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/v1/cart/*")
public class CartApiServlet extends HttpServlet {

    private final ProductService productService;
    private final ObjectMapper mapper;

    public CartApiServlet() {
        this.productService = new ProductService();
        this.mapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        HttpSession session = req.getSession();

        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }

        List<Product> products = productService.getProductsFromCart(cart);

        String cartJson = mapper.writeValueAsString(products);
        resp.setStatus(HttpServletResponse.SC_OK);
        writer.write(cartJson);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        CartItemDto item = mapper.readValue(req.getInputStream(), CartItemDto.class);
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }

        Long id = item.getProductId();
        if (cart.containsKey(id)) {
            cart.put(item.getProductId(), cart.get(id) + item.getQuantity());
        } else {
            cart.put(id, item.getQuantity());
        }

        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contextPath = req.getContextPath();
        PrintWriter writer = resp.getWriter();
        HttpSession session = req.getSession();

        if (contextPath == null || contextPath.equals("/")) {
            writer.write("Wrong path");
            return;
        }

        try {
            Long id = Long.valueOf(contextPath.substring(1));
            Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
            cart.remove(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (Exception e) {
            writer.write("Id must be a number");
        }
    }
}
