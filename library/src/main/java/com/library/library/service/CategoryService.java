package com.library.library.service;

import com.library.library.elasticSearchQuery.ElasticSearchQuery;
import com.library.library.entity.Category;

import com.library.library.excepion.CustomException;
import com.library.library.repository.JpaCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CategoryService {

    JpaCategoryRepository jpaCategoryRepository;
    ElasticSearchQuery elasticSearchQuery;

    public String addCategory(Category category) {

            Boolean checkCategoryExists = jpaCategoryRepository.existsByCategoryName(category.getCategoryName());
            if (checkCategoryExists) {
                throw new CustomException("trying to add category "+category.getCategoryName()+" which is already exists");
            }
            jpaCategoryRepository.save(category);
            return  "Category added successfully";

    }
    public String deleteCategory(Long id) {
            if(!jpaCategoryRepository.existsById(id)) {
                throw new ResourceNotFoundException("trying to delete Category with id "+id+" which is Not exists");
            }
            jpaCategoryRepository.deleteById(id);
            return "Delete Category with id "+id +" successfully";
    }
    public Category getCategoryById(Long id) {
            Category category = jpaCategoryRepository.findById(id).orElseThrow(()->
                    new ResourceNotFoundException("The Category with this id "+id+" is Not exists"));
           return  category;

    }
    public List<Category> getAllCategories() {
        return jpaCategoryRepository.findAll();
    }

    public String updateCategory(Long categoryId, Category category) {

            Category categoryInDB = jpaCategoryRepository.findById(categoryId).orElseThrow(() ->
                    new ResourceNotFoundException("category with id " + categoryId + " Not Found To update it"));
            categoryInDB.setCategoryName(category.getCategoryName());
            jpaCategoryRepository.save(categoryInDB);
            return "The Category with id " + categoryId + " is updated successfully";

    }
}
