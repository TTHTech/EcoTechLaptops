package org.example.test.repository;

import org.example.test.model.Cart;
import org.example.test.model.Customer;
import org.example.test.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findCartById(Long id);
    public Cart findCartByCustomer(Customer customer);
}
