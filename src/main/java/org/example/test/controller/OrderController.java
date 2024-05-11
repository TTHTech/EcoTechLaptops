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
import org.springframework.ui.Model;
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
    public String createOrder(HttpSession session, Model model,
                              @RequestParam("phone_input") String phone_input,
                              @RequestParam("address_input") String address_input,
                              @RequestParam("province_input") String province_input,
                              @RequestParam("payment") String paymentMethod) {

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

        //neu chon thanh toan online thi dan den trang thanh toan
        if (paymentMethod.equals("payment_online")){
            return "redirect:/payment/getPage";
        }
        //neu khong thi dat hang luon
        orderService.createOrder(cart, customer, paymentMethod, "unpaid");
        return "redirect:/home";
    }
}
