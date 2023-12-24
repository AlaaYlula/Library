package com.library.library.DTO;

import com.library.library.Entity.Book;
import com.library.library.Entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    Long id;
    String bookName;
    CategoryDto categoryDto;

    public static BookDto bookDtofromEntity(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setBookName(book.getBookName());

        if (book.getCategory() != null) {
            CategoryDto categoryDTO = new CategoryDto();
            categoryDTO.setId(book.getCategory().getId());
            categoryDTO.setCategoryName(book.getCategory().getCategoryName());
            bookDto.setCategoryDto(categoryDTO);
        }
        return bookDto;
    }
}
