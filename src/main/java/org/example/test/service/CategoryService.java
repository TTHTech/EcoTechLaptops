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

    public Category createCategory(Category category) {
        return this.categoryRepository.save(category);
    }

    public List<Category> getAllCategory() {
        return this.categoryRepository.findAll();
    }

    public void deleteCategory(Long id) {
        this.categoryRepository.deleteById(id);
    }

}
