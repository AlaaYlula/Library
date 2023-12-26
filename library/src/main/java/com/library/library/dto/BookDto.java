package com.library.library.dto;

import com.library.library.entity.Book;


import com.library.library.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    Long id;
    String bookName;
    Category category;

    public static BookDto ModelMapperBook(Book book){
        ModelMapper modelMapperBook = new ModelMapper();
        BookDto bookDto = modelMapperBook.map(book, BookDto.class);
        if(book.getCategory()!=null) {
            bookDto.setCategory(book.getCategory());
        }
        return bookDto;
    }

}
