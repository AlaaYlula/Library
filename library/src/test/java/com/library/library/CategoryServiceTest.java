package com.library.library;

import com.library.library.entity.Category;
import com.library.library.excepion.CustomException;
import com.library.library.repository.JpaCategoryRepository;
import com.library.library.service.CategoryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;



import static org.mockito.BDDMockito.*;
@RunWith(SpringRunner.class)
public class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    JpaCategoryRepository categoryRepository;
    private MockMvc mockMvc;

    @Mock
    private RequestAttributes requestAttributes;

    @Before
    public void setup(){
        RequestContextHolder.setRequestAttributes(requestAttributes);
        this.mockMvc = MockMvcBuilders.standaloneSetup(categoryService).build();
            }
    @Test
    public void testSaveCategorySuccessfully(){
        Category category = new Category(102L , "category test 3");
        String message = "Category added successfully";

        when(categoryRepository.existsByCategoryName(category.getCategoryName())).thenReturn(false);

        Mockito.when(categoryRepository.save(category)).thenReturn(category);

        String addSuccessfully = categoryService.addCategory(category);
        assertEquals(addSuccessfully,message);

    }
    @Test
    public void testCanNotAdd(){
        Category category = new Category(102L , "category test 3");
        when(categoryRepository.existsByCategoryName(category.getCategoryName())).thenReturn(true);
        CustomException customException = assertThrows(CustomException.class,
                () -> categoryService.addCategory(category));

        String message = "trying to add category category test 3 which is already exists";
        assertEquals(message,customException.getMessage());
    }

    @Test
    public void testGetCategoryByIdSuccessfully(){
        String categoryName = "category test 1";
        Category category = new Category(100L , "category test 1");

        Mockito.when(categoryRepository.findById(100L)).thenReturn(Optional.of(category));

        Category categoryFind = categoryService.getCategoryById(100L);
        assertEquals(categoryName,categoryFind.getCategoryName());
    }

    @Test
    public void testCanNotGetCategoryNotExists(){

        when(categoryRepository.findById(100L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                ()-> categoryService.getCategoryById(100L));
        String message = "The Category with this id 100 is Not exists";
        assertEquals(message , exception.getMessage());
    }

    @Test
    public void testGetAllCategory(){
        String categoryName = "category test 1";
        String categoryName2 = "category test 2";
        Category category = new Category(100L , "category test 1");
        Category category2 = new Category(101L , "category test 2");

        List<Category> categoryList = new ArrayList<>(Arrays.asList(category,category2));
        Mockito.when(categoryRepository.findAll()).thenReturn(categoryList);

        List<Category> categoryFind = categoryService.getAllCategories();
        assertNotNull(categoryFind);
        assertEquals(2,categoryFind.size());
        assertEquals(categoryName,categoryFind.get(0).getCategoryName());
        assertEquals(categoryName2,categoryFind.get(1).getCategoryName());
    }

    @Test
    public void testDeleteCategorySuccessfully() {

        Category category = new Category(102L , "category test 3");

        when(categoryRepository.existsByCategoryName(category.getCategoryName())).thenReturn(false);

        Mockito.when(categoryRepository.save(category)).thenReturn(category);

        String addSuccessfully = categoryService.addCategory(category);


        when(categoryRepository.existsById(102L)).thenReturn(true);
        doNothing().when(categoryRepository).deleteById(102L);

        String expectedMessage = "Delete Category with id 102 successfully";
        String deleteMessage = categoryService.deleteCategory(102L);

        assertEquals(expectedMessage, deleteMessage);
    }
    @Test
    public void testDeleteCategoryNotFound() {

        // Mocking behavior for existsById
        when(categoryRepository.existsById(100L)).thenReturn(false);

        Long categoryId = 100L;
        String expectedMessage = "trying to delete Category with id " + categoryId + " which is Not exists";

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> categoryService.deleteCategory(categoryId));

        assertEquals(expectedMessage, exception.getMessage());

        // Verify that deleteById was not called in this case
        verify(categoryRepository, never()).deleteById(categoryId);
    }


    @Test
    public void testUpdateCategorySuccessfully(){
        Category category = new Category(102L , "category test 3");


        Category category2= new Category(102L , "category test 3 update");

        when(categoryRepository.findById(102L)).thenReturn(Optional.of(category2));

        Mockito.when(categoryRepository.save(category2)).thenReturn(category2);

        String expectedMessage = "The Category with id 102 is updated successfully";
        String updateMessage = categoryService.updateCategory(102L,category);

        assertEquals(expectedMessage, updateMessage);

    }


    @Test
    public void testCanNotUpdateNotExist(){
        Category category = new Category(100L , "category test 1");

        when(categoryRepository.findById(100L)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                ()-> categoryService.updateCategory(100L,category));
        String message = "category with id 100 Not Found To update it";
        assertEquals(message , exception.getMessage());
    }

}
