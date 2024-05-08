package org.example.test.controller;

import org.example.test.model.Category;
import org.example.test.model.Product;
import org.example.test.service.CategoryService;
import org.example.test.service.ProductService;
import org.example.test.service.UploadService;
import org.springframework.stereotype.Controller;
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

    public AdminProductController(UploadService uploadService, ProductService productService,
            CategoryService categoryService) {
        this.uploadService = uploadService;
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @PostMapping("/create")
    public String handleCreateProduct(@ModelAttribute("newProduct") Product product,
            @RequestParam("imageProduct") MultipartFile file) {

        String avatar = this.uploadService.handleSaveUploadFile(file, "product");
        product.setImage(avatar);
        String name = product.getCategory().getName();
        double stringPrice = product.getPrice();
        product.setPrice(stringPrice);
        Category category = categoryService.findByName(name);
        if (category == null) {
            category = new Category();
            category.setName(name);
            categoryService.createCategory(category);
        }
        product.setCategory(category);
        this.productService.saveProduct(product);
        return "redirect:/product";
    }

    @GetMapping("/admin/order")
    public String adminOrderPage() {
        return "admin/order";
    }

}
