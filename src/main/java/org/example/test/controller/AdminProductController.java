package org.example.test.controller;

import java.util.List;

import org.example.test.model.Category;
import org.example.test.model.Order;
import org.example.test.model.Product;
import org.example.test.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/admin/product")
    public String adminProductPage(Model model) {
        List<Product> products = productService.getAllProduct();
        List<Category> categories = categoryService.getCategoriesByStatus("on");
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
            products = productService.getProductsContainingName(productName);
        } else if (categoryId != null) {
            // Nếu tìm theo danh mục
            products = productService.getProductsByCategoryId(categoryId);
        } else {
            // Không có thông tin tìm kiếm, hiển thị tất cả sản phẩm
            products = productService.getAllProduct();
        }
        List<Category> categories = categoryService.getCategoriesByStatus("on");
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "admin/product";
    }

    @PostMapping("/admin/product/create")
    public String handleCreateProduct(@ModelAttribute("newProduct") Product product, Model model,
            @RequestParam("imageProduct") MultipartFile file) {
        if (productService.isProductExistsInCategoryAndStatusOn(product.getName(), product.getCategory().getName())) {
            model.addAttribute("error", "Tên sản phẩm đã tồn tại trong danh mục này!");
            List<Category> categories = categoryService.getCategoriesByStatus("on");
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
        product.setStatus("on");
        this.productService.saveProduct(product);
        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/create")
    public String adminCreateProductPage(Model model) {

        List<Category> categories = categoryService.getCategoriesByStatus("on");
        model.addAttribute("categories", categories);
        model.addAttribute("newProduct", new Product());
        return "admin/createProduct";
    }

    @PostMapping("/admin/product/delete")
    public String handleDeleteProduct(@RequestParam("id") Long id, Model model) {
        Product product = productService.getProductById(id);
        if (product != null) {
            // Cập nhật trạng thái của sản phẩm thành "off"
            product.setStatus("off");
            productService.saveProduct(product);
        }
        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/update/{id}")
    public String pageUpdateProduct(@PathVariable("id") Long id, Model model) {
        Product currentProduct = productService.getProductById(id);
        List<Category> categories = categoryService.getCategoriesByStatus("on");
        model.addAttribute("categories", categories);
        model.addAttribute("newProduct", currentProduct);
        return "admin/updateProduct";
    }

    @PostMapping("/admin/product/update")
    public String handleUpdateProduct(@ModelAttribute("newProduct") Product product, Model model,
            @RequestParam("imageProduct") MultipartFile file) {
        Product currentProduct = productService.getProductById(product.getId());
        if (!currentProduct.getName().equals(product.getName())) {
            if (!currentProduct.getName().equals(product.getName())
                    && productService.isProductExistsInCategory(product.getName(), product.getCategory().getName())) {
                // Nếu trùng, thêm thông báo lỗi và trả về trang cập nhật sản phẩm với thông báo
                // lỗi
                model.addAttribute("error", "Tên sản phẩm đã tồn tại trong danh mục này!");
                List<Category> categories = categoryService.getCategoriesByStatus("on");
                model.addAttribute("categories", categories);
                model.addAttribute("newProduct", product);
                return "admin/updateProduct";
            }
        }

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

    @PostMapping("admin/product/deleteAll")
    public String handleDeleteAllProduct(Model model) {
        List<Product> products = productService.getAllProduct();
        for (Product product : products) {
            product.setStatus("off");
            productService.saveProduct(product);
        }
        return "redirect:/admin/product";
    }
}
