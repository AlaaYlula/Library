package com.library.library;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.library.library.Controller.CategoryController;
import com.library.library.Entity.Category;
import com.library.library.Service.CategoryService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
public class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private RequestAttributes requestAttributes;
    @Mock
    private CategoryService categoryService;

    private MockMvc mockMvc;


    @Before
    public void setup(){
        RequestContextHolder.setRequestAttributes(requestAttributes);
        this.mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();

    }

    @Test
    public void getCategoryList() throws Exception {

        String categoryName = "category 1";
        Long id = 3L;
        List<Category> categoryList = new ArrayList<>();

        Mockito.when(categoryService.getAllCategories()).thenReturn(categoryList);
        Assert.assertNotNull(categoryController.getCategoryList());
        Assert.assertNotNull(categoryList);
        Assert.assertNotNull(categoryName);
        Assert.assertNotNull(id);

    }

    @Test
    public void getCategoryById(){

        String categoryName = "category 1";
        Long id = 3L;
        Category category = new Category(1L,"category 1");

        Mockito.when(categoryService.getCategoryById(id)).thenReturn(category);
        Assert.assertNotNull(categoryController.getCategoryById(id));
        Assert.assertNotNull(categoryName);
        Assert.assertNotNull(id);
        Assert.assertNotNull(category);

    }

    @Test
    public void deleteCategoryById() throws JsonProcessingException {

        String categoryName = "Category 1";
        Long id = 3L;

        Mockito.when(categoryService.deleteCategory(id)).thenReturn(String.valueOf(Optional.empty()));
        Assert.assertNotNull(categoryController.deleteCategory(id));
        Assert.assertNotNull(categoryName);
        Assert.assertNotNull(id);
    }

    @Test
    public void addCategory() throws JsonProcessingException {

        String categoryName = "category 2";
        Long idCategory = 3L;
        Category category = new Category(1L,"category test");


        Mockito.when(categoryService.addCategory(category)).thenReturn("Category added successfully");
        Assert.assertNotNull(categoryController.addCategory(category));
        Assert.assertNotNull(categoryName);
        Assert.assertNotNull(idCategory);
        Assert.assertNotNull(category);
    }

    @Test
    public void updateCategory() throws JsonProcessingException {

        String categoryName = "category 2";
        Category category = new Category(1L,"category test");

        Mockito.when(categoryService.updateCategory(1L,category)).thenReturn("The Category with id 1 is updated successfully");
        Assert.assertNotNull(categoryController.updateCategory(1L,category));
        Assert.assertNotNull(categoryName);
        Assert.assertNotNull(category);
    }

}
