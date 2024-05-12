package org.example.test.model;

import java.util.List;

import jakarta.persistence.*;
import org.example.test.service.OrderService;

import java.util.List;

@Entity(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private double price;
    @ManyToOne
    private Category category;
    private String description;
    private String image;
    private String status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Product(String status, String productName, double productPrice, Category category, String description,
            String image) {
        this.name = productName;
        this.price = productPrice;
        this.category = category;
        this.description = description;
        this.image = image;
        this.status = status;
    }

    public Product() {
    }

    // đếm số lượng sản phẩm này đã bán được bao nhiêu cái trong tất cả các đơn
    public int countTheNumberOfProductSold(List<Order> orders) {
        int count = 0;
        for (Order order : orders) {
            List<Item> purchasedItem = order.getPurchasedItems();
            for (Item item : purchasedItem) {
                if (this.id == item.getProduct().getId()) {
                    count = count + item.getQuantity();
                }
            }
        }
        return count;
    }

    // đếm số lượng sản phẩm này đã bán được theo ngày
    public int countTheNumberOfProductSoldByDay(List<Order> ordersByDay) {
        int count = 0;
        for (Order order : ordersByDay) {
            List<Item> purchasedItem = order.getPurchasedItems();
            for (Item item : purchasedItem) {
                if (this.id == item.getProduct().getId()) {
                    count = count + item.getQuantity();
                }
            }
        }
        return count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
