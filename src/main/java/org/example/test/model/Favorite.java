package org.example.test.model;

import jakarta.persistence.*;

import java.util.List;

@Entity(name = "favorites")
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    private Customer customer;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "favorite", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;

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

    public Favorite(Customer customer, List<Product> products) {
        this.customer = customer;
        this.products = products;
    }

    public Favorite() {
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
