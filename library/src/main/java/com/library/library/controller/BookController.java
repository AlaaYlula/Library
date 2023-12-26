package com.library.library.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.library.library.apiResponse.Response;
import com.library.library.dto.BookDto;
import com.library.library.entity.Book;
import com.library.library.service.BookService;
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

    @PostMapping("/")
    public ResponseEntity<Object> addBook(@RequestBody Book book , @RequestParam Long categoryId) throws JsonProcessingException {
        String message = bookService.addBook(book,categoryId);
        return new ResponseEntity<>(new Response(message, HttpStatus.CREATED),HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBook(@PathVariable Long id) throws JsonProcessingException {
        String message = bookService.deleteBook(id);
        return new ResponseEntity<>(new Response(message,HttpStatus.NO_CONTENT),HttpStatus.NO_CONTENT);
    }

    @GetMapping("/")
    public ResponseEntity<Object> getBooksList(){
        List<BookDto> bookDtoList = bookService.getAllBooks();
        return ResponseEntity.ok(bookDtoList);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Object> getBookById(@PathVariable Long id){
        BookDto bookDto = bookService.getBookById(id);
        return ResponseEntity.ok(bookDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateBook(@RequestParam Long categoryId , @PathVariable Long id, @RequestBody Book book) throws JsonProcessingException {
        String message =  bookService.updateBook(categoryId,id,book);
        return new ResponseEntity<>(new Response(message,HttpStatus.CREATED), HttpStatus.CREATED);
    }

}
