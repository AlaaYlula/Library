package com.library.library;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.library.library.DTO.BookDto;
import com.library.library.DTO.CategoryDto;
import com.library.library.ElasticSearchQuery.ElasticSearchQuery;
import com.library.library.Entity.Book;
import com.library.library.Entity.Category;
import com.library.library.Entity.Logs;
import com.library.library.Entity.levelEnum.Level;
import com.library.library.Excepion.CustomException;
import com.library.library.Repository.JpaBookRepository;

import com.library.library.Repository.JpaCategoryRepository;
import com.library.library.Service.BookService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;


import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    JpaBookRepository bookRepository;

    @Mock
    JpaCategoryRepository categoryRepository;
    private MockMvc mockMvc;

    @Mock
    private RequestAttributes requestAttributes;

    // For Elastic Search
    @InjectMocks
    private ElasticSearchQuery elasticSearchQuery;
    @Mock
    private ElasticsearchClient elasticsearchClient;

    @Mock
    private IndexResponse indexResponse;

    @Before
    public void setup(){
        RequestContextHolder.setRequestAttributes(requestAttributes);
        this.mockMvc = MockMvcBuilders.standaloneSetup(bookRepository).build();

    }

  @Test
    public void testAddBookSuccessfully() throws Exception {
      String message = "The Book added successfully";
      Book book = new Book(100L,"book test");
      Category category = new Category(10L,"category test");

      when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
      when(bookRepository.save(book)).thenReturn(book);
      String addMessage = bookService.addBook(book,10L);

      assertEquals(message,addMessage);

  }

    @Test
    public void testAddBookAlreadyInTheCategory() throws Exception {
        String message = "trying to add Book which already in this category";
        Book book = new Book(100L,"book test");
        Category category = new Category(10L,"category test");
        List<Book> bookList = new ArrayList<>();
        bookList.add(book);
        category.setBookSet(bookList);
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        CustomException exception = assertThrows(CustomException.class,
                ()-> bookService.addBook(book,10L));
        assertEquals(message,exception.getMessage());

    }

    @Test
    public void testAddBookInNotExistsCategory() throws Exception {
        String message = "category with id 10 Not Found";
        Book book = new Book(100L,"book test");
        Category category = new Category(10L,"category test");

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                ()-> bookService.addBook(book,10L));
        assertEquals(message,exception.getMessage());

    }

  @Test
    public void testDeleteBookSuccessfully() throws IOException {
      String message = "Delete Book with id 100 successfully" ;

      when(bookRepository.existsById(100L)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(100L);
        String deleteMessage = bookService.deleteBook(100L);
        assertEquals(message,deleteMessage);
      // Mock ElasticsearchClient.createDocumentLog to return a success message
      Date date = new Date();
      Logs log = new Logs(message, Level.INFO, date);

      //when(indexResponse.index()).thenReturn("logsearch");

      when(elasticsearchClient.index(i -> i
              .index("logsearch")
              .id(log.getId())
              .document(log))).thenReturn(indexResponse);

      String result = elasticSearchQuery.createDocumentLog(log);
      assertEquals("Create Document Log", result);
      //assertEquals("logsearch",indexResponse.index());

  }

  @Test
    public void testDeleteBookNotFound() throws IOException {
        String message = "Delete Book with id 100 which is Not exists";
        when(bookRepository.existsById(100L)).thenReturn(false);

      ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class ,
              ()-> bookService.deleteBook(100L));

      assertEquals(message,exception.getMessage());

      // Mock ElasticsearchClient.createDocumentLog to return a success message
      Date date = new Date();
      Logs log = new Logs(message, Level.ERROR, date);

      when(elasticsearchClient.index(i -> i
              .index("logsearch")
              .id(log.getId())
              .document(log))).thenReturn(indexResponse);

      String result = elasticSearchQuery.createDocumentLog(log);
      assertEquals("Create Document Log", result);
  }

  @Test
    public void testGetBookById(){
      Book book = new Book(100L,"book test");
        when(bookRepository.findById(100L)).thenReturn(Optional.of(book));

      BookDto bookDto = BookDto.ModelMapperBook(book);
      BookDto bookReturned = bookService.getBookById(100L);

      assertEquals(bookDto.getBookName(),bookReturned.getBookName());
      assertNotNull(bookReturned);
  }

  @Test
  public void testNotGetBookById(){
        String message = "The Book with this id 100 is Not exists";
      when(bookRepository.findById(100L)).thenReturn(Optional.empty());
      ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class ,
              ()-> bookService.getBookById(100L));
      assertEquals(message,exception.getMessage());
  }
  @Test
    public void testGetAllBooks() throws IOException {
        Book book1 = new Book(100L,"test book1");
      Book book2 = new Book(101L,"test book2");
        List<Book> bookList = new ArrayList<>(Arrays.asList(book1,book2));

      when(bookRepository.findAll()).thenReturn(bookList);

      List<BookDto> bookDTOList = bookList.stream()
              .map(book -> {
                  BookDto bookDto = BookDto.ModelMapperBook(book);
                  return bookDto;
              })
              .toList();
      List<BookDto> bookDtoListRecived = bookService.getAllBooks();
      assertEquals(bookDTOList.size(),bookDtoListRecived.size());
      assertEquals(bookDTOList.get(0).getBookName(),bookDtoListRecived.get(0).getBookName());
      assertEquals(bookDTOList.get(0).getId(),bookDtoListRecived.get(0).getId());
      assertEquals(bookDTOList.get(1).getBookName(),bookDtoListRecived.get(1).getBookName());
      assertEquals(bookDTOList.get(1).getId(),bookDtoListRecived.get(1).getId());

  }


    @Test
    public void testUpdateBook(){

      String message = "Book with id 100 updated successfully";
      Book book = new Book(100L,"test book1");

      when(bookRepository.findById(100L)).thenReturn(Optional.of(book));
      when(bookRepository.save(book)).thenReturn(book);

      String updateMessaga = bookService.updateBook(100L,book);
      assertEquals(message,updateMessaga);
  }

  @Test
    public void testCanNotUpdate(){
      Book book = new Book(100L,"test book1");

      String message = "Can't find Book with id 100 to update";
      when(bookRepository.findById(100L)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class ,
                ()-> bookService.updateBook(100L,book));
        assertEquals(message,exception.getMessage());
  }
}
