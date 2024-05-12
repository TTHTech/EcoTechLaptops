package org.example.test.service;

import java.util.List;

import org.example.test.model.Product;
import org.example.test.repository.ProductRepository;
import org.example.test.repository.ItemRepository;
import org.example.test.repository.FavoriteRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ItemRepository itemRepository;
    private final FavoriteRepository favoriteRepository;

    public ProductService(FavoriteRepository favoriteRepository, ItemRepository itemRepository,
            ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.itemRepository = itemRepository;
        this.favoriteRepository = favoriteRepository;
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

    public int getProductCountByCategoryId(long id) {
        return this.productRepository.countProductByCategoryId(id);
    }

    public List<Product> getProductsByName(String productName) {
        return this.productRepository.findProductsByName(productName);
    }

    public List<Product> searchProduct(String input) {
        return productRepository.findByNameContainingAndStatus(input, "on");
    }

    public void deleteAllProduct() {
        this.productRepository.deleteAll();
    }

    public boolean isProductExistsInCategory(String name, String name2) {
        return this.productRepository.existsProductByNameAndCategoryName(name, name2);

    }

    public boolean isProductInUse(Long productId) {
        return itemRepository.existsByProduct_Id(productId)
                || favoriteRepository.existsByFavoriteProducts_Id(productId);
    }

    public boolean areAnyProductsInUse() {
        return itemRepository.existsByProduct_IdIsNotNull()
                || favoriteRepository.existsByFavoriteProducts_IdIsNotNull();
    }

    public int getCountOfProductsByCategoryIdAndStatus(long id, String string) {
        return productRepository.countProductByCategoryIdAndStatus(id, string);
    }

    public boolean isProductExistsInCategoryAndStatusOn(String name, String name2) {
        return this.productRepository.existsProductByNameAndCategoryNameAndStatus(name, name2, "on");
    }

    public List<Product> getProductsContainingName(String productName) {
        return this.productRepository.findProductsByNameContaining(productName);
    }


    public List<Product> getAllProductByStatus(String status) {
        return productRepository.findByStatus(status);
    }

}
