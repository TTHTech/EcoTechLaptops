package org.example.test.controller;

import jakarta.servlet.http.HttpSession;
import org.example.test.model.Cart;
import org.example.test.model.Category;
import org.example.test.model.Customer;
import org.example.test.model.Product;
import org.example.test.repository.CustomerRepository;
import org.example.test.service.CartService;
import org.example.test.service.CategoryService;
import org.example.test.service.CustomerServiceRegister;
import org.example.test.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    public String getHomePage(Model model, HttpSession session) {
//        List<Product> products = productService.getAllProduct();
//        List<Category> categories = categoryService.getAllCategory();
//        model.addAttribute("products", products);
//        model.addAttribute("categories", categories);

        Customer customer = (Customer) session.getAttribute("customer");
        System.out.println(">>>check session lay tu login22: " + customer.getEmail());
        model.addAttribute("customer", customer);

        List<Product> productList = (List<Product>) session.getAttribute("products");
        List<Category> categoryList = (List<Category>) session.getAttribute("categories");
        model.addAttribute("products", productList);
        model.addAttribute("categories", categoryList);

        return "home/index";
    }

    @GetMapping("/addToCart/{productId}")
    public String addToCart(@PathVariable Long productId, HttpSession session){
        Cart cart = (Cart) session.getAttribute("cart");
        System.out.println(">>>check product add to cart: " + cart.getId() + " - productID: " + productId + " customer: " + cart.getCustomer().getEmail());
        cartService.addToCart(cart.getId(), productId);

        // Lấy lại giỏ hàng mới từ cơ sở dữ liệu
        cart = cartService.findCart(cart.getCustomer());

        // Cập nhật giỏ hàng mới vào phiên làm việc
        session.setAttribute("cart", cart);

//        return "redirect:/home";
        return "redirect:/cart/getCart";
    }
}
