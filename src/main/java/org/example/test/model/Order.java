package org.example.test.model;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private Customer customer;
    @OneToMany
    private List<Item> purchasedItems;
    private String status;
    private Date createDate;
    private String paymentMethod;
    private String paymentStatus;

    public Order() {
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customer=" + customer +
                ", purchasedItems=" + purchasedItems +
                ", status='" + status + '\'' +
                ", createDate=" + createDate +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Item> getPurchasedItems() {
        return purchasedItems;
    }

    public void setPurchasedItems(List<Item> purchasedItems) {
        this.purchasedItems = purchasedItems;
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

    public Order(Customer customer, List<Item> purchasedItems, String status, Date createDate, String paymentMethod, String paymentStatus) {
        this.customer = customer;
        this.purchasedItems = purchasedItems;
        this.status = status;
        this.createDate = createDate;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
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
    public String getPaymentStatus() {
        return paymentStatus;
    }
    
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public double getTotalPrice(){
        double totalPrice = 0;
        for (Item item : purchasedItems){
            totalPrice += item.getProduct().getPrice() * item.getQuantity();
        }
        return totalPrice;
    }

    // đếm số lượng sản phẩm có trong đơn hàng này
    public int getTheNumberOfProduct() {
        int count = 0;
        for (Item item : purchasedItems){
            count = count + item.getQuantity();
        }
        return count;
    }
}
