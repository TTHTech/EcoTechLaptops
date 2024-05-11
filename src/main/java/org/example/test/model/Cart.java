package org.example.test.model;

import jakarta.persistence.*;

import java.util.List;

@Entity(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    private Customer customer;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Item> items;

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

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Cart(Customer customer, List<Item> items) {
        this.customer = customer;
        this.items = items;
    }

    public Cart() {
    }
    public double getTotalPrice(){
        double totalPrice = 0;
        for (int i=0; i<items.size(); i++){
            totalPrice += items.get(i).getQuantity() * items.get(i).getProduct().getPrice();
        }
        return totalPrice;
    }
}
