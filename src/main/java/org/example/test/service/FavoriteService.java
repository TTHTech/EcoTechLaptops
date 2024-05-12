package org.example.test.service;

import org.example.test.model.Favorite;
import org.example.test.model.Product;
import org.example.test.model.Customer;
import org.example.test.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    public Favorite getFavoriteWithProductsOnStatus(Customer customer) {
        Favorite favorite = findByCustomer(customer);
        if (favorite == null) {
            return null; // Return null if no favorite list is found
        }

        List<Product> products = new ArrayList<>(favorite.getFavoriteProducts());
        boolean[] needUpdate = {false}; // Use an array to hold the flag

        // Filter products to retain only those with the status 'on'
        products.removeIf(product -> {
            boolean toRemove = !"on".equals(product.getStatus());
            if (toRemove) {
                needUpdate[0] = true; // Set the flag if any product is removed
            }
            return toRemove;
        });

        // If there are changes in the list, update the database
        if (needUpdate[0]) {
            favorite.setFavoriteProducts(products);
            favorite = favoriteRepository.save(favorite);
        }

        return favorite;
    }



    public boolean isProductInUse(long id) {
        return favoriteRepository.existsByFavoriteProducts_Id(id);
    }

}
