package org.example.test.model;
import jakarta.persistence.*;

import java.util.Date;

@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private Cart cart;
    private String status;
    private Date createDate;
    private String paymentMethod;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Order(Cart cart, String status, Date createDate, String paymentMethod) {
        this.cart = cart;
        this.status = status;
        this.createDate = createDate;
        this.paymentMethod = paymentMethod;
    }

    public Order() {
    }
}
