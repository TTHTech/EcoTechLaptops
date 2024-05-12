package org.example.test.service;

import org.example.test.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    @Autowired
    private OrderService orderService;

    public void makePayment(Order order){

    }
}
