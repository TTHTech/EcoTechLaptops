package org.example.test.controller;

import jakarta.servlet.http.HttpSession;
import org.example.test.model.*;
import org.example.test.repository.CustomerRepository;
import org.example.test.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping(path = "/home")
public class HomeController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CartService cartService;
    @Autowired
    private FavoriteService favoriteService;

    @GetMapping("/addToFavorite/{productId}")
    public String addToFavorite(@PathVariable Long productId, HttpSession session){
        Customer customer = (Customer) session.getAttribute("customer");
        Product product = productService.getProductById(productId);

        favoriteService.addProductToFavorites(customer, product);

        return "redirect:/favorite/getFavorite";
    }
    @GetMapping("")
    public String getHomePage(Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        System.out.println(">>>check session lay tu login22: " + customer.getEmail());
        model.addAttribute("customer", customer);

        List<Product> productList = (List<Product>) session.getAttribute("products");
        List<Category> categoryList = (List<Category>) session.getAttribute("categories");

        // Tạo một bản sao của danh sách sản phẩm gốc
        List<Product> featureProduct = new ArrayList<>(productList);
        // Trộn danh sách sản phẩm
        Collections.shuffle(featureProduct, new Random());
        // Lấy 10 sản phẩm đầu tiên từ danh sách đã trộn
        List<Product> randomProducts = featureProduct.subList(0, Math.min(8, featureProduct.size()));

        model.addAttribute("featureProducts", randomProducts);
        model.addAttribute("products", randomProducts);
        model.addAttribute("categories", categoryList);

        return "home/index";
    }

//    @GetMapping("")
//    public String getHomePage(Model model, HttpSession session) {
//        Customer customer = (Customer) session.getAttribute("customer");
//        System.out.println(">>>check session lay tu login22: " + customer.getEmail());
//        model.addAttribute("customer", customer);
//
//        List<Product> productList = (List<Product>) session.getAttribute("products");
//        List<Category> categoryList = (List<Category>) session.getAttribute("categories");
//
//        // Trộn danh sách sản phẩm
//        Collections.shuffle(productList, new Random());
//
//        // Chia danh sách sản phẩm thành hai phần
//        int numberOfProducts = Math.min(50, productList.size()); // Lấy tối đa 32 sản phẩm, nếu có ít hơn thì lấy hết
//        List<Product> featureProducts = productList.subList(0, Math.min(28, numberOfProducts));
//        List<Product> remainingProducts = productList.subList(Math.min(22, numberOfProducts), numberOfProducts);
//
//        model.addAttribute("featureProducts", featureProducts);
//        model.addAttribute("products", remainingProducts);
//        model.addAttribute("categories", categoryList);
//
//        return "home/index";
//    }

    @GetMapping("/addToCart/{productId}")
    public String addToCart(@PathVariable Long productId, HttpSession session){
        Cart cart = (Cart) session.getAttribute("cart");
        System.out.println(">>>check product add to cart: " + cart.getId() + " - productID: " + productId + " customer: " + cart.getCustomer().getEmail());
        cartService.addToCart(cart.getId(), productId);

        // Lấy lại giỏ hàng mới từ cơ sở dữ liệu
        cart = cartService.getCartWithProductsOnStatus(cart.getCustomer());

        // Cập nhật giỏ hàng mới vào phiên làm việc
        session.setAttribute("cart", cart);

//        return "redirect:/home";
        return "redirect:/cart/getCart";
    }

    @GetMapping("/shop")
    public String getShop(HttpSession session, Model model){
        Customer customer = (Customer) session.getAttribute("customer");
        Cart cart = (Cart) session.getAttribute("cart");

        List<Product> listProductFound = (List<Product>) session.getAttribute("listProductFound");
        if (listProductFound == null) {
            //lấy toàn bộ sản phẩm trong kho
            listProductFound = (List<Product>) session.getAttribute("products");
        }

        model.addAttribute("customer", customer);
        model.addAttribute("products", listProductFound);
        model.addAttribute("cart", cart);

        //xoá session list found đó để lần sau dùng list all product cho trang shop
        session.removeAttribute("listProductFound");

        return "home/shop";
    }
}
