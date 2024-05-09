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

    @PostMapping("/admin/product/delete")
    public String handleDeleteProduct(@RequestParam("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/create")
    public String handleCreatProduct(Model model) {
        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("categories", categories);
        model.addAttribute("newProduct", new Product());
        return "admin/createProduct";
    }

    @PostMapping("/admin/product/update")
    public String handleUpdateProduct(@ModelAttribute("newProduct") Product product,
            @RequestParam("imageProduct") MultipartFile file) {
        Product currentProduct = productService.getProductById(product.getId());
        currentProduct.setName(product.getName());
        currentProduct.setPrice(product.getPrice());
        currentProduct.setDescription(product.getDescription());
        Category category = categoryService.findByName(product.getCategory().getName());
        String imageProduct = this.uploadService.handleSaveUploadFile(file, "product");
        currentProduct.setImage(imageProduct);
        currentProduct.setCategory(category);
        productService.saveProduct(currentProduct);
        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/update/{id}")
    public String pageUpdateProduct(@PathVariable("id") Long id, Model model) {
        Product currentProduct = productService.getProductById(id);
        List<Category> categories = categoryService.getAllCategory();

        model.addAttribute("categories", categories);
        model.addAttribute("newProduct", currentProduct);
        return "admin/updateProduct";
    }

    @GetMapping("/admin/category")
    public String adminCategoryPage(Model model) {
        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("categories", categories);
        return "admin/category";
    }

    @PostMapping("/admin/category/delete")
    public String handleDeleteCategory(@RequestParam("id") Long id) {
        Category category = categoryService.getCategoryById(id);
        if (category != null) {
            List<Product> products = productService.getProductsByCategoryId(id);
            for (Product product : products) {
                productService.deleteProduct(product.getId());
            }
            categoryService.deleteCategory(id);
        }
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
        categoryService.saveCategory(category);
        return "redirect:/admin/category";
    }

    @GetMapping("/admin/category/update/{id}")
    public String pageUpdateCategory(@PathVariable("id") Long id, Model model) {
        Category currentCategory = categoryService.getCategoryById(id);
        model.addAttribute("newCategory", currentCategory);
        return "admin/updateCategory";
    }

    @PostMapping("/admin/category/update")
    public String handleUpdateCategory(@ModelAttribute("newCategory") Category category,
            @RequestParam("imageProduct") MultipartFile file) {
        Category currentCategory = categoryService.getCategoryById(category.getId());
        currentCategory.setName(category.getName());
        String imageCategory = this.uploadService.handleSaveUploadFile(file, "category");
        currentCategory.setImage(imageCategory);
        categoryService.saveCategory(currentCategory);
        return "redirect:/admin/category";
    }

}
