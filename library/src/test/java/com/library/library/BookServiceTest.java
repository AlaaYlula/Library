package com.library.library;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.library.dto.BookDto;
import com.library.library.elasticSearchQuery.ElasticSearchQuery;
import com.library.library.entity.Book;
import com.library.library.entity.Category;
import com.library.library.entity.Logs;
import com.library.library.entity.levelEnum.Level;
import com.library.library.excepion.CustomException;
import com.library.library.repository.JpaBookRepository;

import com.library.library.repository.JpaCategoryRepository;
import com.library.library.service.BookService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;


import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

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

    ObjectMapper objectMapper;
    @Before
    public void setup(){
        RequestContextHolder.setRequestAttributes(requestAttributes);
        this.mockMvc = MockMvcBuilders.standaloneSetup(bookRepository).build();
         objectMapper = new ObjectMapper();
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
        String message = "trying to add Book book test which is already in the category test category";
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
//    @Test
//    public void testAddBookWithNoCategoryParam() throws DataIntegrityViolationException {
////        MissingServletRequestParameterException exception = assertThrows(MissingServletRequestParameterException.class,
////                () -> mockMvc.perform(MockMvcRequestBuilders.post("/book/add")
////                                .content("{\"bookName\":\"book test\"}")
////                                .contentType("application/json"))
////                        .andExpect(status().isBadRequest())
////                        .andReturn());
////        String expectedMessage = "Required request parameter 'categoryId' for method parameter type Long is not present";
////
////        MissingServletRequestParameterException exception = assertThrows(MissingServletRequestParameterException.class,()->
////                mockMvc.perform(post("/book/add")
////                                .content("{\"bookName\":\"book test\"}")
////                                .contentType(MediaType.APPLICATION_JSON))
////                        .andExpect(status().isBadRequest())
////                        .andExpect(content()
////                                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
////                      );
//        // Check the error message or any other assertions you need
//      //  String expectedMessage = "Required request parameter 'categoryId' for method parameter type Long is not present";
//        String actualMessage = exception.getMessage();
//        // Add any other assertions based on your needs
//        assertEquals(expectedMessage, actualMessage);
//
//    }

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
        String message = "The Book with id 100 is Not exists";
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
    public void testUpdateBookSuccessfully(){

      String message = "Book with id 100 updated successfully";
      Book book = new Book(100L,"test book1");
      Category category = new Category(100L,"category 1");

      when(bookRepository.findById(100L)).thenReturn(Optional.of(book));
        when(categoryRepository.findById(100L)).thenReturn(Optional.of(category));

        when(bookRepository.save(book)).thenReturn(book);

      String updateMessaga = bookService.updateBook(100L,100L,book);
      assertEquals(message,updateMessaga);
  }

  @Test
    public void testCanNotUpdateBookNotFound(){
      Book book = new Book(100L,"test book1");
      Category category = new Category(100L,"category 1");

      String message = "Can't find Book with id 100 to update";
      when(bookRepository.findById(100L)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class ,
                ()-> bookService.updateBook(100L,100L,book));
        assertEquals(message,exception.getMessage());
  }

    @Test
    public void testCanNotUpdateBookCategoryNotFound(){
        Book book = new Book(100L,"test book1");
        Category category = new Category(100L,"category 1");
        String message = "You can't update the category for book: "+book.getBookName()+", the category with id "+category.getId()+" Not Found";

        when(bookRepository.findById(100L)).thenReturn(Optional.of(book));
        when(categoryRepository.findById(100L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class ,
                ()-> bookService.updateBook(100L,100L,book));
        assertEquals(message,exception.getMessage());
    }
}
