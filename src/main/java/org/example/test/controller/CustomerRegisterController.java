package org.example.test.controller;


import org.example.test.model.Cart;
import org.example.test.model.Customer;
import org.example.test.model.Item;
import org.example.test.service.CartService;
import org.example.test.service.CustomerServiceRegister;
import org.example.test.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/register")
public class CustomerRegisterController {

    private final CustomerServiceRegister customerService;  // Corrected type

    @Autowired
    private ItemService itemService;
    @Autowired
    private CartService cartService;

    @Autowired
    public CustomerRegisterController(CustomerServiceRegister customerService) {  // Corrected constructor name
        this.customerService = customerService;
    }

    @ModelAttribute("customer")
    public Customer customer() {
        return new Customer();
    }

    @GetMapping
    public String showRegistrationForm() {
        return "home/register";
    }

    @PostMapping
    public String registerCustomerAccount(@ModelAttribute("customer") Customer customer, RedirectAttributes redirectAttributes) {
        try {
            customerService.save(customer);  // Corrected service call

            //tạo cái cart cho user đó sau khi nó đăng ký tài khoản
            List<Item> itemList = new ArrayList<>();
            Cart cart = new Cart(customer, itemList);
            cartService.saveCart(cart);
            System.out.println("Created Cart for this customer (" + customer.getId() + ")!");

            return "redirect:/register?success";
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/register?error";
        }
    }
}
