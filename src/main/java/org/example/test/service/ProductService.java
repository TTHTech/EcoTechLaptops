package org.example.test.service;

import java.util.List;

import org.example.test.model.Product;
import org.example.test.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product saveProduct(Product product) {
        return this.productRepository.save(product);
    }

    public List<Product> getAllProduct() {
        return this.productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return this.productRepository.findProductById(id);
    }

    @Transactional
    public void deleteProduct(Long id) {
        this.productRepository.deleteProductById(id);
    }

    public List<Product> getProductsByCategoryId(Long id) {
        return this.productRepository.findProductsByCategoryId(id);
    }

}
