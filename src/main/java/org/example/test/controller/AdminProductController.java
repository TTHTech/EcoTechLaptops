package org.example.test.controller;

import java.util.List;

import org.example.test.model.Category;
import org.example.test.model.Order;
import org.example.test.model.Product;
import org.example.test.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class AdminProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final UploadService uploadService;
    private final OrderService orderService;
    private final CartService cartService;

    public AdminProductController(UploadService uploadService, ProductService productService,
                                  CategoryService categoryService, OrderService orderService, CartService cartService) {
        this.uploadService = uploadService;
        this.productService = productService;
        this.categoryService = categoryService;
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @PostMapping("/admin/product/create")
    public String handleCreateProduct(@ModelAttribute("newProduct") Product product, Model model,
            @RequestParam("imageProduct") MultipartFile file) {
        if (productService.isProductExistsInCategory(product.getName(), product.getCategory().getName())) {
            model.addAttribute("error", "Tên sản phẩm đã tồn tại trong danh mục này!");
            List<Category> categories = categoryService.getAllCategory();
            model.addAttribute("categories", categories);
            model.addAttribute("newProduct", new Product());
            return "admin/createProduct"; // Trả về trang tạo sản phẩm với thông báo lỗi
        }
        String imageProduct = this.uploadService.handleSaveUploadFile(file, "product");
        product.setImage(imageProduct);
        double stringPrice = product.getPrice();
        product.setPrice(stringPrice);
        Category category = this.categoryService.findByName(product.getCategory().getName());
        product.setCategory(category);
        this.productService.saveProduct(product);
        return "redirect:/admin/product";
    }
}
