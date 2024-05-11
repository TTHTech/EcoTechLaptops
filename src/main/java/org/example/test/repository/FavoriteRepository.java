package org.example.test.repository;

import org.example.test.model.Favorite;
import org.example.test.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Favorite findByCustomer(Customer customer);

    boolean existsByFavoriteProducts_Id(Long productId);

    boolean existsByFavoriteProducts_IdIsNotNull();
}
