package org.example.test.service;

import org.example.test.model.Cart;
import org.example.test.model.Customer;
import org.example.test.model.Item;
import org.example.test.model.Order;
import org.example.test.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartService cartService;

    public String createOrder(Cart cart, Customer customer, String paymentMethod){
        //tao order moi
        Order order = new Order(customer, cart.getItems(), "pending approval", new Date(), paymentMethod);
        orderRepository.save(order);

        //xoa list item ra khoi cart
        System.out.println(cartService.deleteAllItems(cart));

        return "Created order!";
    }
}
