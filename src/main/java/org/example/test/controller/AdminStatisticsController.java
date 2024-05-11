package org.example.test.controller;

import org.example.test.model.Order;
import org.example.test.service.OrderService;
import org.example.test.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminStatisticsController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;


    @GetMapping("/admin/statistics")
    public String adminStatistics(Model model) {
        model.addAttribute("filterBy", "TẤT CẢ");
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("totalPriceOfAllOrders", orderService.getTotalPriceOfAllOrders());
        model.addAttribute("theNumberOfAllProductsSold", orderService.getTheNumberOfProductsSoldInAllOrders());
        model.addAttribute("products", productService.getAllProduct());
        return "admin/statistics";
    }

    @GetMapping("/admin/statistics/day")
    public String adminStatistics_filterByDay(Model model) {
        model.addAttribute("filterBy", "THEO NGÀY HÔM NAY");
        model.addAttribute("totalPriceOfAllOrders", orderService.getTotalPriceOfAllOrdersByDay());
        model.addAttribute("theNumberOfAllProductsSold", orderService.getTheNumberOfProductsSoldInAllOrdersByDay());
        model.addAttribute("orders", orderService.getAllOrdersByDay());
        model.addAttribute("products", productService.getAllProduct());
        return "admin/statistics";
    }

    @GetMapping("/admin/statistics/week")
    public String adminStatistics_filterByWeek(Model model) {
        model.addAttribute("filterBy", "THEO TUẦN NÀY");
        model.addAttribute("orders", orderService.getAllOrdersByWeek());
        model.addAttribute("totalPriceOfAllOrders", orderService.getTotalPriceOfAllOrdersByWeek());
        model.addAttribute("theNumberOfAllProductsSold", orderService.getTheNumberOfProductsSoldInAllOrdersByWeek());
        model.addAttribute("products", productService.getAllProduct());
        return "admin/statistics";
    }

    @GetMapping("/admin/statistics/month")
    public String adminStatistics_filterByMonth(Model model) {
        model.addAttribute("filterBy", "THEO THÁNG NÀY");
        model.addAttribute("orders", orderService.getAllOrdersByMonth());
        model.addAttribute("totalPriceOfAllOrders", orderService.getTotalPriceOfAllOrdersByMonth());
        model.addAttribute("theNumberOfAllProductsSold", orderService.getTheNumberOfProductsSoldInAllOrdersByMonth());
        model.addAttribute("products", productService.getAllProduct());
        return "admin/statistics";
    }
}
