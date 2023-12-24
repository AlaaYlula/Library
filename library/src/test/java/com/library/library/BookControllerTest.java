package com.library.library;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.library.library.Controller.BookController;
import com.library.library.DTO.BookDto;
import com.library.library.DTO.CategoryDto;
import com.library.library.Entity.Book;
import com.library.library.Service.BookService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RunWith(SpringRunner.class)
public class BookControllerTest {

    @InjectMocks
    private BookController bookController;

    @Mock
    private RequestAttributes requestAttributes;
    @Mock
    private BookService bookService;

    private MockMvc mockMvc;


    @Before
    public void setup(){
        RequestContextHolder.setRequestAttributes(requestAttributes);
        this.mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();

    }

    @Test
    public void getBookList() throws Exception {

        String bookName = "book 2";
        Long id = 3L;
        List<BookDto> bookDtoList = new ArrayList<>();

        Mockito.when(bookService.getAllBooks()).thenReturn(bookDtoList);
        Assert.assertNotNull(bookController.getBooksList());
        Assert.assertNotNull(bookDtoList);
        Assert.assertNotNull(bookName);
        Assert.assertNotNull(id);

    }

    @Test
    public void getBookById(){

        String bookName = "book 2";
        Long id = 3L;
        CategoryDto category = new CategoryDto(1L,"cate 1 ");
        BookDto bookDto = new BookDto();
        Mockito.when(bookService.getBookById(id)).thenReturn(bookDto);
        Assert.assertNotNull(bookController.getBookById(id));
        Assert.assertNotNull(bookDto);
        Assert.assertNotNull(bookName);
        Assert.assertNotNull(id);
        Assert.assertNotNull(category);

    }

    @Test
    public void deleteBookById() throws JsonProcessingException {

        String bookName = "book 2";
        Long id = 3L;

        Mockito.when(bookService.deleteBook(id)).thenReturn(String.valueOf(Optional.empty()));
        Assert.assertNotNull(bookController.deleteBook(id));
        Assert.assertNotNull(bookName);
        Assert.assertNotNull(id);
    }

    @Test
    public void addBook() throws JsonProcessingException {

        String bookName = "book 2";
        Long idCategory = 3L;
        Book book = new Book(1L,"book test");


        Mockito.when(bookService.addBook(book,idCategory)).thenReturn("The Book added successfully");
        Assert.assertNotNull(bookController.addBook(book,idCategory));
        Assert.assertNotNull(bookName);
        Assert.assertNotNull(idCategory);
        Assert.assertNotNull(book);
    }

    @Test
    public void updateBook() throws JsonProcessingException {

        String bookName = "book 2";
        Book book = new Book(1L,"book upadte");


        Mockito.when(bookService.updateBook(1L,book)).thenReturn("Book with id 1 updated successfully");
        Assert.assertNotNull(bookController.updateBook(1L,book));
        Assert.assertNotNull(bookName);
        Assert.assertNotNull(book);
    }

}
