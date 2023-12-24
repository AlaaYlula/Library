package com.library.library.DTO;

import com.library.library.Entity.Book;


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
    CategoryDto categoryDto;

    public static BookDto ModelMapperBook(Book book){
        ModelMapper modelMapperBook = new ModelMapper();
        BookDto bookDto = modelMapperBook.map(book, BookDto.class);
        if(book.getCategory()!=null) {
            ModelMapper modelMapper = new ModelMapper();
            CategoryDto categoryDto= modelMapper.map(book.getCategory(), CategoryDto.class);
            bookDto.setCategoryDto(categoryDto);
        }
        return bookDto;
    }

}
