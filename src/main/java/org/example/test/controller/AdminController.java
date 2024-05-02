package org.example.test.controller;

import java.util.List;

import org.example.test.model.Category;
import org.example.test.model.Product;
import org.example.test.service.CategoryService;
import org.example.test.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminController {
    private final ProductService productService;
    private final CategoryService categoryService;

    public AdminController(CategoryService categoryService,ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping("/admin")
    public String adminHomePage() {
        return "admin/index";
    }

    @GetMapping("/product")
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
}
