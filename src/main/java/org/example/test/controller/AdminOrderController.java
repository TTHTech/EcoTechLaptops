package org.example.test.controller;

import org.example.test.model.Item;
import org.example.test.model.Order;
import org.example.test.service.ItemService;
import org.example.test.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AdminOrderController {
    private OrderService orderService;
    private ItemService itemService;

    public AdminOrderController(OrderService orderService, ItemService itemService) {
        this.orderService = orderService;
        this.itemService = itemService;
    }

    @GetMapping("/admin/order")
    public String openOrderManagementPage(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/order";
    }

    @GetMapping("/admin/order/{id}")
    public String openUdateOrderStatusPage(@PathVariable int id, Model model) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        //List<Item> items = order.getCart().getItems();
        //model.addAttribute("items", items);
        return "admin/update-order-status";
    }

    @PostMapping("/admin/order/{id}")
    public String updateOrderStatus(@PathVariable int id, @ModelAttribute("order") Order order) {
        Order existingOrder = orderService.getOrderById(id);
        existingOrder.setStatus(order.getStatus());
        orderService.updateOrder(existingOrder);
        return "redirect:/admin/order";
    }
}
