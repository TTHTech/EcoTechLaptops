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


import java.text.SimpleDateFormat;
import java.util.*;


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
        String currentDay = getCurrentDay();
        return orderRepository.findOrdersByDate(currentDay);
    }

    // lấy tất cả đơn hàng trong tuần của ngày hiện tại
    public List<Order> getAllOrdersByWeek() {
        Date today_date = new Date();
        // lấy số tuần trong tháng của ngày hiện tại
        int todayWeekNumber = getWeekOfMonth(today_date);
        int month = today_date.getMonth();
        int year = today_date.getYear();

        List<Order> orders = orderRepository.findAll();
        List<Order> ordersByWeek = new ArrayList<Order>();
        for (Order order : orders) {
            Date orderCreateDate = order.getCreateDate();
            // kiểm tra đơn hàng phải cùng tháng và cùng ngày
            if (orderCreateDate.getMonth() == month && orderCreateDate.getYear() == year) {
                // sau đó bắt đầu kiểm tra dơn hàng phải cùng số tuần trong tháng
                int orderWeekNumber = getWeekOfMonth(orderCreateDate);
                if (orderWeekNumber == todayWeekNumber) {
                    ordersByWeek.add(order);
                }
            }
        }
        return ordersByWeek;
    }

    public List<Order> getAllOrdersByMonth() {
        // lấy tháng và năm của ngày hôm nay
        Date today_date = new Date();
        int month = today_date.getMonth();
        int year = today_date.getYear();
        List<Order> orders = orderRepository.findAll();
        List<Order> ordersByMonth = new ArrayList<Order>();
        for (Order order : orders) {
            Date orderCreateDate = order.getCreateDate();
            // kiểm tra đơn hàng phải cùng tháng và cùng năm
            if (orderCreateDate.getMonth() == month && orderCreateDate.getYear() == year) {
                ordersByMonth.add(order);
            }
        }
        return ordersByMonth;
    }

    public String getCurrentDay() {
        Date today_date = new Date();
        String today_string = new SimpleDateFormat("yyyy-MM-dd").format(today_date);
        return today_string;
    }



    // lấy ra số tuần của trong 1 tháng
    public int getWeekOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int weekNumberOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);
        return weekNumberOfMonth;
    }


    public double getTotalPriceOfAllOrdersByWeek() {
        double total = 0;
        for (Order order : getAllOrdersByWeek()) {
            total = total + order.getTotalPrice();
        }
        return total;
    }

    public int getTheNumberOfProductsSoldInAllOrdersByWeek() {
        int count = 0;
        for (Order order : getAllOrdersByWeek()) {
            count = count + order.getTheNumberOfProduct();
        }
        return count;
    }

    public double getTotalPriceOfAllOrdersByMonth() {
        double total = 0;
        for (Order order : getAllOrdersByMonth()) {
            total = total + order.getTotalPrice();
        }
        return total;
    }

    public int getTheNumberOfProductsSoldInAllOrdersByMonth() {
        int count = 0;
        for (Order order : getAllOrdersByMonth()) {
            count = count + order.getTheNumberOfProduct();
        }
        return count;
    }
}
