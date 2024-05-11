package org.example.test.service;

import org.example.test.model.Cart;
import org.example.test.model.Customer;
import org.example.test.model.Item;

import org.aspectj.weaver.ast.Or;
import org.example.test.model.Order;
import org.example.test.repository.CartRepository;

import org.example.test.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private CartRepository cartRepository;
  
    public String createOrder(Cart cart, Customer customer, String paymentMethod, String paymentStatus){
        //tao order moi
        Order order = new Order(customer, cart.getItems(), "pending approval", new Date(), paymentMethod, paymentStatus);
        orderRepository.save(order);

        //xoa list item ra khoi cart
        System.out.println(cartService.deleteAllItems(cart));

        return "Created order!";
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(long id) {
        Optional<Order> optional = orderRepository.findById(id);
        Order order = null;
        if (optional.isPresent()) {
            order = optional.get();
        } else {
            throw new RuntimeException("Order is not found for id: " + id);
        }
        return order;
    }

    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }
}
