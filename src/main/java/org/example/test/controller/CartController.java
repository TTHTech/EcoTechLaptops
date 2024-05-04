package org.example.test.controller;

import org.example.test.model.Cart;
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

    @GetMapping("/getCart/{id}")
    public String getCart(@PathVariable Long id, Model model){
        Cart cart = cartService.getCart(id);
        model.addAttribute("cart", cart);
        model.addAttribute("totalPrice", cart.getTotalPrice());

        return "home/cart";
    }

    // + (-)
    @GetMapping("/updateCart/{cartId}/{itemId}/{quantity}")
    public String updateQuantityCart(@PathVariable Long cartId, @PathVariable Long itemId, @PathVariable int quantity){
        cartService.updateQuantityCart(cartId, itemId, quantity);

        return "redirect:/cart/getCart/" + cartId;
    }

    @GetMapping("/deleteItem/{cartId}/{itemId}")
    public String deleteItemById(@PathVariable Long cartId, @PathVariable Long itemId){
        this.cartService.deleteItemById(cartId,itemId);

        return "redirect:/cart/getCart/" + cartId;
    }

}


