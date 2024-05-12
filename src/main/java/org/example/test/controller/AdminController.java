package org.example.test.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.test.model.Category;
import org.example.test.model.Customer;
import org.example.test.model.Product;
import org.example.test.service.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AdminController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final UploadService uploadService;
    private final CustomerService customerService;

    public AdminController(CustomerService customerService,
            UploadService uploadService,
            CategoryService categoryService,
            ProductService productService, CartService cartService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.uploadService = uploadService;
        this.customerService = customerService;

    }

    @GetMapping("/admin")
    public String adminHomePage() {
        return "admin/index";
    }

    @PostMapping("/admin/category")
    public String adminCategoryPage(
            @RequestParam(value = "categoryName", required = false) String categoryName,
            Model model) {
        List<Category> categories;
        if (categoryName == null || categoryName.isEmpty()) {
            categories = categoryService.getAllCategory();
        } else
            categories = this.categoryService.getCategoriesContainingName(categoryName);
        model.addAttribute("categories", categories);
        Map<Long, Integer> productCountMap = new HashMap<>();
        for (Category category : categories) {
            int productCount = productService.getProductCountByCategoryId(category.getId());
            productCountMap.put(category.getId(), productCount);
        }
        model.addAttribute("categories", categories);
        model.addAttribute("productCountMap", productCountMap);
        return "admin/category";
    }

    @GetMapping("/admin/category")
    public String adminCategoryPage(Model model) {
        List<Category> categories = categoryService.getAllCategory();
        Map<Long, Integer> productCountMap = new HashMap<>();
        for (Category category : categories) {
            int productCount = productService.getCountOfProductsByCategoryIdAndStatus(category.getId(), "on");
            productCountMap.put(category.getId(), productCount);
        }
        model.addAttribute("categories", categories);
        model.addAttribute("productCountMap", productCountMap);
        return "admin/category";
    }

    @PostMapping("admin/category/deleteAll")
    public String handleDeleteAllCategory() {

        List<Product> products = productService.getAllProduct();
        for (Product product : products) {
            product.setStatus("off");
            productService.saveProduct(product);
        }
        List<Category> categories = categoryService.getAllCategories();
        for (Category category : categories) {
            category.setStatus("off");
            categoryService.saveCategory(category);
        }
        return "redirect:/admin/category";
    }

    @PostMapping("/admin/category/delete")
    public String handleDeleteCategory(@RequestParam("id") Long id, Model model) {
        Category category = categoryService.getCategoryById(id);
        if (category != null) {
            List<Product> products = productService.getProductsByCategoryId(id);
            for (Product product : products) {
                product.setStatus("off");
                productService.saveProduct(product);
            }
            category.setStatus("off");
            categoryService.saveCategory(category);
        }

        return "redirect:/admin/category";
    }

    @GetMapping("/admin/category/create")
    public String handleCreateCategory(Model model) {
        model.addAttribute("newCategory", new Category());
        return "admin/createCategory";
    }

    @PostMapping("/admin/category/create")
    public String handleCreateCategory(@ModelAttribute("newCategory") Category category, Model model,
            @RequestParam("imageCategory") MultipartFile file) {

        if (categoryService.isCategoryExistsAndStatusOn(category.getName())) {
            model.addAttribute("error", "Tên danh mục đã tồn tại !");
            return "admin/createCategory";
        }
        String image = this.uploadService.handleSaveUploadFile(file, "category");
        category.setImage(image);
        category.setStatus("on");
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
    public String handleUpdateCategory(@ModelAttribute("newCategory") Category category, Model model,
            @RequestParam("imageProduct") MultipartFile file) {
        Category currentCategory = categoryService.getCategoryById(category.getId());
        if (!currentCategory.getName().equals(category.getName())) {
            if (!currentCategory.getName().equals(category.getName())
                    && categoryService.isCategoryExists(category.getName())) {
                model.addAttribute("error", "Tên danh mục đã tồn tại !");
                model.addAttribute("newCategory", category);
                return "admin/updateCategory";
            }
        }

        currentCategory.setName(category.getName());
        String imageCategory = this.uploadService.handleSaveUploadFile(file, "category");
        if (imageCategory != "")
            currentCategory.setImage(imageCategory);
        categoryService.saveCategory(currentCategory);
        return "redirect:/admin/category";
    }

    @GetMapping("/admin/customer")
    public String adminUserPage(Model model) {
        List<Customer> customers = customerService.getAllCustomer();
        model.addAttribute("customers", customers);
        return "admin/customer";
    }
}
