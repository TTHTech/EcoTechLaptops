package org.example.test.controller;

import org.example.test.model.Category;
import org.example.test.model.Product;
import org.example.test.service.CategoryService;
import org.example.test.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;
import java.util.Random;

@Controller
public class GuestController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/guest")
    public String getGuestPage(Model model) {
        // Lấy tất cả sản phẩm
        List<Product> productList = productService.getAllProduct();
        // Lấy tất cả danh mục
        List<Category> categoryList = categoryService.getAllCategory();

        // Trộn danh sách sản phẩm để hiển thị ngẫu nhiên
        Collections.shuffle(productList, new Random());
        // Lấy 10 sản phẩm đầu tiên từ danh sách đã trộn
        List<Product> randomProducts = productList.subList(0, Math.min(8, productList.size()));

        model.addAttribute("products", randomProducts);
        model.addAttribute("categories", categoryList);

        return "home/guest";
    }
}

