package org.example.test.controller;

import jakarta.servlet.http.HttpSession;
import org.example.test.model.Cart;
import org.example.test.model.Customer;
import org.example.test.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/getCart")
    public String getCart(Model model, HttpSession session){
        Cart cart = (Cart) session.getAttribute("cart");
        model.addAttribute("cart", cart);
        model.addAttribute("totalPrice", cart.getTotalPrice());

        return "home/cart";
    }

    // + (-)
    @GetMapping("/updateCart/{itemId}/{quantity}")
    public String updateQuantityCart(@PathVariable Long itemId, @PathVariable int quantity, HttpSession session){
        Cart cart = (Cart) session.getAttribute("cart");
        cartService.updateQuantityCart(cart.getId(), itemId, quantity);

        // Lấy lại giỏ hàng mới từ cơ sở dữ liệu
        cart = cartService.findCart(cart.getCustomer());

        // Cập nhật giỏ hàng mới vào phiên làm việc
        session.setAttribute("cart", cart);

        return "redirect:/cart/getCart";
    }

    @GetMapping("/deleteItem/{itemId}")
    public String deleteItemById(@PathVariable Long itemId, HttpSession session){
        Cart cart = (Cart) session.getAttribute("cart");
        this.cartService.deleteItemById(cart.getId(),itemId);

        // Lấy lại giỏ hàng mới từ cơ sở dữ liệu
        cart = cartService.findCart(cart.getCustomer());

        // Cập nhật giỏ hàng mới vào phiên làm việc
        session.setAttribute("cart", cart);

        return "redirect:/cart/getCart";
    }

}


