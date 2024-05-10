package org.example.test.controller;

import jakarta.servlet.http.HttpSession;
import org.example.test.model.Cart;
import org.example.test.model.Customer;
import org.example.test.repository.CartRepository;
import org.example.test.service.CartService;
import org.example.test.service.CustomerServiceRegister;
import org.example.test.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;
    @Autowired
    private CustomerServiceRegister customerService;

    @PostMapping("/createOrder")
    public String createOrder(HttpSession session,
                              @RequestParam("phone_input") String phone_input,
                              @RequestParam("address_input") String address_input,
                              @RequestParam("province_input") String province_input,
                              @RequestParam("payment") String payment) {

        Customer customer = (Customer) session.getAttribute("customer");
        //update customer-info
        if (!Objects.equals(customer.getPhone(), phone_input)) {
            customer.setPhone(phone_input);
        }
        if (!Objects.equals(customer.getAddress(), address_input)) {
            customer.setAddress(address_input + ", " + province_input);
        }
        Cart cart = (Cart) session.getAttribute("cart");
        cart.setCustomer(customer);
        customerService.updateCustomer(customer);
        cartService.saveCart(cart);

        //check
        System.out.println(">>check customer14: "+ customer.getId());
        System.out.println(">>check cart14: "+ cart.getId());
        System.out.println(">>check input14: "+ phone_input + "/" + address_input + "/" + province_input + "/" + payment);

        //Dat hang
        orderService.createOrder(cart, customer, payment);

        //return ve trang home tam thoi
        return "redirect:/home";
    }
}
