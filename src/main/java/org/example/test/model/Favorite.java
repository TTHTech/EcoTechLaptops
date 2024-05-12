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
    @OneToMany
    private List<Product> favoriteProducts;

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

    public List<Product> getFavoriteProducts() {
        return favoriteProducts;
    }

    // Trong class Favorite, sửa tên phương thức thành setFavoriteProducts
    public void setFavoriteProducts(List<Product> favoriteProducts) {
        this.favoriteProducts = favoriteProducts;
    }


    public Favorite(Customer customer, List<Product> favoriteProducts) {
        this.customer = customer;
        this.favoriteProducts = favoriteProducts;
    }

    public Favorite() {
    }
}
