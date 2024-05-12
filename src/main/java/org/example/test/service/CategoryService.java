package org.example.test.service;

import org.example.test.model.Category;
import org.example.test.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category findByName(String name) {
        return this.categoryRepository.findCategoryByName(name);
    }

    public Category saveCategory(Category category) {
        return this.categoryRepository.save(category);
    }

    public List<Category> getAllCategory() {
        return this.categoryRepository.findAll();
    }

    public void deleteCategory(Long id) {
        this.categoryRepository.deleteById(id);
    }

    public Category getCategoryById(Long id) {
        return this.categoryRepository.findCategoryById(id);
    }

    public List<Category> getCategoriesByName(String name) {
        return this.categoryRepository.findCategoriesByName(name);
    }

    public void deleteAllProduct() {
        this.categoryRepository.deleteAll();
    }

    public boolean isCategoryExists(String name) {
        return this.categoryRepository.existsCategoryByName(name);
    }

    public List<Category> getAllCategories() {
        return this.categoryRepository.findAll();
    }

    public List<Category> getCategoriesByStatus(String string) {
        return this.categoryRepository.findCategoriesByStatus(string);
    }

    public boolean isCategoryExistsAndStatusOn(String name) {
        return this.categoryRepository.existsCategoryByNameAndStatus(name, "on");
    }

    public List<Category> getCategoriesContainingName(String categoryName) {
        return this.categoryRepository.findCategoriesByNameContaining(categoryName);
    }

    public List<Category> getAllCategoryByStatus(String status) {
        return categoryRepository.findByStatus(status);
    }

}
