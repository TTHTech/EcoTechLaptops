package org.example.test.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.test.model.Category;
import org.example.test.model.Customer;
import org.example.test.model.Product;
import org.example.test.service.CategoryService;
import org.example.test.service.ProductService;
import org.example.test.service.UploadService;
import org.example.test.service.CustomerService;

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

    public AdminController(CustomerService customerService, UploadService uploadService,
            CategoryService categoryService,
            ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.uploadService = uploadService;
        this.customerService = customerService;
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

    @PostMapping("/admin/product")
    public String adminProductPage(@RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "productName", required = false) String productName,
            Model model) {
        List<Product> products;
        if (productName != null && !productName.isEmpty()) {
            // Nếu tìm theo tên sản phẩm
            products = productService.getProductsByName(productName);
        } else if (categoryId != null) {
            // Nếu tìm theo danh mục
            products = productService.getProductsByCategoryId(categoryId);
        } else {
            // Không có thông tin tìm kiếm, hiển thị tất cả sản phẩm
            products = productService.getAllProduct();
        }
        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "admin/product";
    }

    @PostMapping("/admin/category")
    public String adminCategoryPage(
            @RequestParam(value = "categoryName", required = false) String categoryName,
            Model model) {
        List<Category> categories;
        if (categoryName == null || categoryName.isEmpty()) {
            categories = categoryService.getAllCategory();
        } else
            categories = this.categoryService.getCategoriesByName(categoryName);
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
        if (imageProduct != "")
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
        Map<Long, Integer> productCountMap = new HashMap<>();
        for (Category category : categories) {
            int productCount = productService.getProductCountByCategoryId(category.getId());
            productCountMap.put(category.getId(), productCount);
        }
        model.addAttribute("categories", categories);
        model.addAttribute("productCountMap", productCountMap);
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
