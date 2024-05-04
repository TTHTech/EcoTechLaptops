package org.example.test.controller;

import org.example.test.model.Category;
import org.example.test.model.Product;
import org.example.test.service.CartService;
import org.example.test.service.CategoryService;
import org.example.test.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(path = "/home")
public class HomeController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CartService cartService;

    @GetMapping("")
    public String getHomePage(Model model) {
        List<Product> products = productService.getAllProduct();
        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);

        return "home/index";
    }

    @GetMapping("/addToCart/{cartId}/{productId}")
    public String addToCart(@PathVariable Long cartId, @PathVariable Long productId){
        System.out.println(">>>check product add to cart: " + cartService.getCart(cartId) + " - productID: " + productId);
        cartService.addToCart(cartId, productId);

//        return "redirect:/home";
        return "redirect:/cart/getCart/" + cartId;
    }
}
