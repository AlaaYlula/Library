package com.library.library.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.library.library.Entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    Long id;
    String categoryName;
}
