package org.example.test.repository;

import java.util.List;

import org.example.test.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findProductById(Long id);

    void deleteProductById(Long id);

    List<Product> findProductsByCategoryId(Long id);

    int countProductByCategoryId(long id);

    List<Product> findProductsByName(String productName);

    // search:
    List<Product> findByNameContainingAndStatus(String input, String status);

    boolean existsProductByNameAndCategoryName(String name, String name2);

    int countProductByCategoryIdAndStatus(long id, String string);

    boolean existsProductByNameAndCategoryNameAndStatus(String name, String name2, String string);


    List<Product> findProductsByNameContaining(String productName);
    public List<Product> findByStatus(String status);

}
