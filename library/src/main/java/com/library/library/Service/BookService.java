package com.library.library.Service;

import com.library.library.DTO.BookDto;
import com.library.library.ElasticSearchQuery.ElasticSearchQuery;
import com.library.library.Entity.Book;
import com.library.library.Entity.Category;
import com.library.library.Excepion.CustomException;
import com.library.library.Repository.JpaBookRepository;
import com.library.library.Repository.JpaCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.util.List;

@Service
@AllArgsConstructor
public class BookService {

    JpaBookRepository bookRepository;

    JpaCategoryRepository categoryRepository;

    ElasticSearchQuery elasticSearchQuery;

    public String addBook(Book book, Long categoryId){
        try{
            Category category = categoryRepository.findById(categoryId).orElseThrow(()->
                    new ResourceNotFoundException("category with id "+categoryId+" Not Found"));
            if(category.getBookSet()!=null) {
                boolean checkbookInCategoryExists = category.getBookSet().stream()
                        .anyMatch(bookN -> bookN.getBookName() != null && bookN.getBookName().equals(book.getBookName()));
                if (checkbookInCategoryExists) {
                    throw new CustomException("trying to add Book which already in this category");
                }
            }
            book.setCategory(category);
            bookRepository.save(book);
            return "The Book added successfully";
        }catch (ResourceNotFoundException  ex) {
            throw new ResourceNotFoundException(ex.getMessage());
        }catch (CustomException e ){
            throw  new CustomException(e.getMessage());
        }
    }

    public String deleteBook(Long id) {
            if(!bookRepository.existsById(id)) {
                throw new ResourceNotFoundException("Delete Book with id "+id+" which is Not exists");
            }
            bookRepository.deleteById(id);
            String message = "Delete Book with id "+id +" successfully" ;
            return message;
    }
    public BookDto getBookById(Long id) {
            Book book = bookRepository.findById(id).orElseThrow(()->
                    new ResourceNotFoundException("The Book with this id "+id+" is Not exists"));
        BookDto bookDto = BookDto.ModelMapperBook(book);
        return bookDto;
    }

    public List<BookDto> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        List<BookDto> bookDTOList = books.stream()
                .map(book -> {
                   BookDto bookDto = BookDto.ModelMapperBook(book);
                    return bookDto;
                })
                .toList();
        return bookDTOList;
    }


    // This will update only the book name
    public String updateBook(Long categoryId, Long id, Book book) {
        try {
            Book bookFind = bookRepository.findById(id).orElseThrow(() ->
                    new ResourceNotFoundException("Can't find Book with id " + id + " to update"));

            Category category = categoryRepository.findById(categoryId).orElseThrow(()->
                    new ResourceNotFoundException("You can't update the category for book: "+bookFind.getBookName()+", the category with id "+categoryId+" Not Found"));
            bookFind.setBookName(book.getBookName());
            bookFind.setCategory(category);
            bookRepository.save(bookFind);
            return "Book with id " + id + " updated successfully";
        }catch (ResourceNotFoundException ex){
            throw  new ResourceNotFoundException(ex.getMessage());
        }catch (Exception e){
            throw  new CustomException(e.getMessage());
        }

    }
}
