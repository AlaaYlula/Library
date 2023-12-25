package com.library.library.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.library.library.AppResponse.Response;
import com.library.library.DTO.BookDto;
import com.library.library.Entity.Book;
import com.library.library.Service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "book")
public class BookController {

    BookService bookService;

    @PostMapping("/add")
    public ResponseEntity<Object> addBook(@RequestBody Book book , @RequestParam Long categoryId) throws JsonProcessingException {
        String message = bookService.addBook(book,categoryId);
        String jsonString = Response.convertToJsonString(message,HttpStatus.CREATED);
        return new ResponseEntity<>(jsonString,HttpStatus.CREATED);
        //return new ResponseEntity<>(new Response(message, HttpStatus.CREATED),HttpStatus.CREATED);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteBook(@PathVariable Long id) throws JsonProcessingException {
        String message = bookService.deleteBook(id);
        String jsonString = Response.convertToJsonString(message,HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(jsonString,HttpStatus.NO_CONTENT);
       // return new ResponseEntity<>(new Response(message,HttpStatus.NO_CONTENT),HttpStatus.NO_CONTENT);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getBooksList(){
        List<BookDto> bookDtoList = bookService.getAllBooks();
        return ResponseEntity.ok(bookDtoList);
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getBookById(@PathVariable Long id){
        BookDto bookDto = bookService.getBookById(id);
        return ResponseEntity.ok(bookDto);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateBook(@RequestParam Long categoryId , @PathVariable Long id, @RequestBody Book book) throws JsonProcessingException {
        String message =  bookService.updateBook(categoryId,id,book);
        String jsonString = Response.convertToJsonString(message,HttpStatus.CREATED);
        return new ResponseEntity<>(jsonString,HttpStatus.CREATED);
     //   return new ResponseEntity<>(new Response(message,HttpStatus.CREATED), HttpStatus.CREATED);
    }

}
