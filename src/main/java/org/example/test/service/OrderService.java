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

    // tính tổng tiền của tất cả các hóa đơn có trong database
    public double getTotalPriceOfAllOrders() {
        double total = 0;
        for (Order order : getAllOrders()) {
            total = total + order.getTotalPrice();
        }
        return total;
    }

    /*
    tính tổng tiền của tất cả các hóa đơn trong database
    nhưng chỉ lấy hóa đơn ở hiện tại
    */
    public double getTotalPriceOfAllOrdersByDay() {
        double total = 0;
        for (Order order : getAllOrdersByDay()) {
            total = total + order.getTotalPrice();
        }
        return total;
    }

    // đếm số lượng của tất cả sản phẩm có trong tất cả hóa đơn
    public int getTheNumberOfProductsSoldInAllOrders() {
        int count = 0;
        for (Order order : getAllOrders()) {
            count = count + order.getTheNumberOfProduct();
        }
        return count;
    }

    // đếm số lượng của tất cả sản phẩm có trong tất cả hóa đơn theo ngày
    public int getTheNumberOfProductsSoldInAllOrdersByDay() {
        int count = 0;
        for (Order order : getAllOrdersByDay()) {
            count = count + order.getTheNumberOfProduct();
        }
        return count;
    }

    public List<Order> getAllOrdersByDay() {
        return orderRepository.findOrdersByDate("2024-05-10");
    }
}
