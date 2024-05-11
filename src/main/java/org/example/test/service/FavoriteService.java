package org.example.test.service;

import org.example.test.model.Favorite;
import org.example.test.model.Product;
import org.example.test.model.Customer;
import org.example.test.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private ProductService productService;
    public Favorite findByCustomer(Customer customer) {
        return favoriteRepository.findByCustomer(customer);
    }
    @Transactional
    public void addProductToFavorites(Customer customer, Product product) {
        Favorite favorite = favoriteRepository.findByCustomer(customer);
        if (favorite == null) {
            favorite = new Favorite(customer, new ArrayList<>());
        }

        if (!favorite.getFavoriteProducts().contains(product)) {
            favorite.getFavoriteProducts().add(product);
            favorite = favoriteRepository.save(favorite);
        }
    }
    @Transactional
    public void removeProductFromFavorites(Customer customer, Product product) {
        Favorite favorite = favoriteRepository.findByCustomer(customer);
        if (favorite != null && favorite.getFavoriteProducts().contains(product)) {
            favorite.getFavoriteProducts().remove(product);
            favoriteRepository.save(favorite);
        }
    }

}
