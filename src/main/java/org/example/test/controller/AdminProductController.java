package org.example.test.controller;

import org.example.test.model.Category;
import org.example.test.model.Order;
import org.example.test.model.Product;
import org.example.test.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public String handleCreateProduct(@ModelAttribute("newProduct") Product product,
            @RequestParam("imageProduct") MultipartFile file) {

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
