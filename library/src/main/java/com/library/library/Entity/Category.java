package com.library.library.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String categoryName;

    @OneToMany( mappedBy = "category", cascade = CascadeType.ALL)
    private List<Book> bookSet;

    public Category(String categoryName, List<Book> bookSet) {
        this.categoryName = categoryName;
        this.bookSet = bookSet;
    }

    public Category(Long id , String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }
}
