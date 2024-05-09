package org.example.test.service;

import org.aspectj.weaver.ast.Or;
import org.example.test.model.Order;
import org.example.test.repository.CartRepository;
import org.example.test.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(int id) {
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
