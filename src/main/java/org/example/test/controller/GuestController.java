package org.example.test.controller;

import org.example.test.model.Product;
import org.example.test.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class GuestController {
    @Autowired
    private ProductService productService;

    @GetMapping("/guest")
    public String getHomePage(Model model) {
        List<Product> productg = productService.getAllProduct();
        model.addAttribute("productg", productg);
        return "home/guest";  // Giả sử rằng trang chủ của bạn sử dụng template "index" trong thư mục "home"
    }
}
