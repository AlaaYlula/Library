package com.library.library.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.library.library.apiResponse.Response;
import com.library.library.entity.Category;
import com.library.library.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "category")
public class CategoryController {

    CategoryService categoryService;

    @PostMapping("/")
    public ResponseEntity<Object> addCategory(@RequestBody Category category) throws JsonProcessingException {
        String message = categoryService.addCategory(category);
        return new ResponseEntity<>(new Response(message, HttpStatus.CREATED),HttpStatus.CREATED);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable Long id) throws JsonProcessingException {
         String message = categoryService.deleteCategory(id);
         return new ResponseEntity<>(new Response(message,HttpStatus.NO_CONTENT),HttpStatus.NO_CONTENT);

    }
    @GetMapping("/{id}")
    public ResponseEntity<Object> getCategoryById(@PathVariable Long id){
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);

    }
    @GetMapping("/")
    public ResponseEntity<Object> getCategoryList(){
        List<Category> categoryList =  categoryService.getAllCategories();
        return ResponseEntity.ok(categoryList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCategory(@PathVariable Long id , @RequestBody Category category ) throws JsonProcessingException {
        String message =  categoryService.updateCategory(id,category);
        return new ResponseEntity<>(new Response(message,HttpStatus.CREATED),HttpStatus.CREATED);

    }



}
