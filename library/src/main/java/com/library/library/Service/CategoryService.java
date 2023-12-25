package com.library.library.Service;

import com.library.library.ElasticSearchQuery.ElasticSearchQuery;
import com.library.library.Entity.Category;

import com.library.library.Excepion.CustomException;
import com.library.library.Repository.JpaCategoryRepository;
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
        try {
//            List<Category> categoryList = jpaCategoryRepository.findAll();
//            boolean checkCategoryExists = categoryList.stream()
//                    .anyMatch(catName -> catName.getCategoryName() != null && catName.getCategoryName().equals(category.getCategoryName()));
            Boolean checkCategoryExists = jpaCategoryRepository.existsByCategoryName(category.getCategoryName());
            if (checkCategoryExists) {
                throw new CustomException("trying to add category which is already exists");
            }
            jpaCategoryRepository.save(category);
            return  "Category added successfully";

        }catch (Exception e){
            throw  new CustomException(e.getMessage());
        }


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
        List<Category> categoryList = jpaCategoryRepository.findAll();
        return  categoryList ;
    }

    // Only update the Name of category
    public String updateCategory(Long categoryId, Category category) {
        try {
            Category categoryInDB = jpaCategoryRepository.findById(categoryId).orElseThrow(() ->
                    new ResourceNotFoundException("category with id " + categoryId + " Not Found To update it"));
            categoryInDB.setCategoryName(category.getCategoryName());
            jpaCategoryRepository.save(categoryInDB);
            return "The Category with id " + categoryId + " is updated successfully";
        }catch (ResourceNotFoundException ex){
            throw new ResourceNotFoundException(ex.getMessage());
        }catch (Exception e){
            throw new CustomException(e.getMessage());
        }
    }
}
