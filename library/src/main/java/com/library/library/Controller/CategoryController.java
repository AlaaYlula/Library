package com.library.library.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.library.AppResponse.Response;
import com.library.library.Entity.Category;
import com.library.library.Service.CategoryService;
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

    @PostMapping("/add")
    public ResponseEntity<Object> addCategory(@RequestBody Category category) throws JsonProcessingException {
        String message = categoryService.addCategory(category);
        String jsonString = Response.convertToJsonString(message,HttpStatus.CREATED);
        return new ResponseEntity<>(jsonString,HttpStatus.CREATED);
      //  return new ResponseEntity<>(new Response(message, HttpStatus.CREATED),HttpStatus.CREATED);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable Long id) throws JsonProcessingException {
         String message = categoryService.deleteCategory(id);
         String jsonString = Response.convertToJsonString(message,HttpStatus.NO_CONTENT);
         return new ResponseEntity<>(jsonString,HttpStatus.NO_CONTENT);
         //return new ResponseEntity<>(new Response(message,HttpStatus.NO_CONTENT),HttpStatus.NO_CONTENT);

    }
    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getCategoryById(@PathVariable Long id){
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);

    }
    @GetMapping("/all")
    public ResponseEntity<Object> getCategoryList(){
        List<Category> categoryList =  categoryService.getAllCategories();
        return ResponseEntity.ok(categoryList);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateCategory(@PathVariable Long id , @RequestBody Category category ) throws JsonProcessingException {
        String message =  categoryService.updateCategory(id,category);
        String jsonString = Response.convertToJsonString(message,HttpStatus.CREATED);
        return new ResponseEntity<>(jsonString,HttpStatus.CREATED);
       // return new ResponseEntity<>(new Response(message,HttpStatus.CREATED),HttpStatus.CREATED);

    }



}
