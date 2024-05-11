package org.example.test.controller;

import jakarta.servlet.http.HttpSession;
import org.example.test.model.Cart;
import org.example.test.model.Customer;
import org.example.test.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "/payment")
public class PaymentController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/getPage")
    public String getPage(HttpSession session, Model model){
        Customer customer = (Customer) session.getAttribute("customer");
        Cart cart = (Cart) session.getAttribute("cart");
        model.addAttribute("customer", customer);
        model.addAttribute("cart", cart);
        return "home/payment";
    }
    @PostMapping("/makePayment")
    public String makePayment(HttpSession session,
                              @RequestParam("bank") String bank,
                              @RequestParam("cardNumber") String cardNumber){
        Customer customer = (Customer) session.getAttribute("customer");
        Cart cart = (Cart) session.getAttribute("cart");
        String paymentStatus = "paid - " + bank + " - " + cardNumber;
        orderService.createOrder(cart, customer, "payment_online", paymentStatus);

        return "redirect:/home";
    }
}
