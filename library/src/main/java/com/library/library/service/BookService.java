package com.library.library.service;

import com.library.library.dto.BookDto;
import com.library.library.elasticSearchQuery.ElasticSearchQuery;
import com.library.library.entity.Book;
import com.library.library.entity.Category;
import com.library.library.excepion.CustomException;
import com.library.library.repository.JpaBookRepository;
import com.library.library.repository.JpaCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BookService {

    JpaBookRepository bookRepository;

    JpaCategoryRepository categoryRepository;

    ElasticSearchQuery elasticSearchQuery;

    public String addBook(Book book, Long categoryId) {

            Category category = categoryRepository.findById(categoryId).orElseThrow(()->
                    new ResourceNotFoundException("category with id "+categoryId+" Not Found"));
            if(category.getBookSet()!=null) {
                boolean checkbookInCategoryExists = category.getBookSet().stream()
                        .anyMatch(bookN -> bookN.getBookName() != null && bookN.getBookName().equals(book.getBookName()));
                if (checkbookInCategoryExists) {
                    throw new CustomException("trying to add Book "+book.getBookName()+" which is already in the "+category.getCategoryName()+" category");
                }
            }
            book.setCategory(category);
            bookRepository.save(book);
            return "The Book added successfully";

    }

    public String deleteBook(Long id) {
            if(!bookRepository.existsById(id)) {
                throw new ResourceNotFoundException("Delete Book with id "+id+" which is Not exists");
            }
            bookRepository.deleteById(id);
        return "Delete Book with id "+id +" successfully";
    }
    public BookDto getBookById(Long id) {
            Book book = bookRepository.findById(id).orElseThrow(()->
                    new ResourceNotFoundException("The Book with id "+id+" is Not exists"));
        return BookDto.ModelMapperBook(book);
    }

    public List<BookDto> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(BookDto::ModelMapperBook)
                .toList();
    }


    public String updateBook(Long categoryId, Long id, Book book) {

            Book bookFind = bookRepository.findById(id).orElseThrow(() ->
                    new ResourceNotFoundException("Can't find Book with id " + id + " to update"));

            Category category = categoryRepository.findById(categoryId).orElseThrow(()->
                    new ResourceNotFoundException("You can't update the category for book: "+bookFind.getBookName()+", the category with id "+categoryId+" Not Found"));
            bookFind.setBookName(book.getBookName());
            bookFind.setCategory(category);
            bookRepository.save(bookFind);
            return "Book with id " + id + " updated successfully";

    }
}
