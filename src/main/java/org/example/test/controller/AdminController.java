package org.example.test.controller;

import java.util.List;

import org.example.test.model.Category;
import org.example.test.model.Product;
import org.example.test.service.CategoryService;
import org.example.test.service.ProductService;
import org.example.test.service.UploadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AdminController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final UploadService uploadService;

    public AdminController(UploadService uploadService, CategoryService categoryService,
            ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.uploadService = uploadService;
    }

    @GetMapping("/admin")
    public String adminHomePage() {
        return "admin/index";
    }

    @GetMapping("/admin/product")
    public String adminProductPage(Model model) {
        List<Product> products = productService.getAllProduct();
        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);

        return "admin/product";
    }

    @PostMapping("/delete")
    public String handleDeleteProduct(@RequestParam("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/product";
    }

    @GetMapping("/create")
    public String handleCreatProduct(Model model) {
        model.addAttribute("newProduct", new Product());
        return "admin/create";
    }

    @PostMapping("/update")
    public String handleUpdateProduct(@ModelAttribute Product product) {
        Product currentProduct = productService.getProductById(product.getId());
        currentProduct.setName(product.getName());
        currentProduct.setPrice(product.getPrice());
        productService.saveProduct(currentProduct);
        return "redirect:/product";
    }

    @GetMapping("/admin/category")
    public String adminCategoryPage(Model model) {
        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("categories", categories);
        return "admin/category";
    }

    @PostMapping("/admin/category/delete")
    public String handleDeleteCategory(@RequestParam("id") Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/admin/category";
    }

    @GetMapping("/admin/category/create")
    public String handleCreateCategory(Model model) {
        model.addAttribute("newCategory", new Category());
        return "admin/createCategory";
    }

    @PostMapping("/admin/category/create")
    public String handleCreateCategory(@ModelAttribute("newCategory") Category category,
            @RequestParam("imageCategory") MultipartFile file) {
        String image = this.uploadService.handleSaveUploadFile(file, "category");
        category.setImage(image);
        categoryService.createCategory(category);
        return "redirect:/admin/category";
    }
}
