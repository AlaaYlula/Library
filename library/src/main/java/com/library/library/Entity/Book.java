package com.library.library.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String bookName;

    @ManyToOne(fetch = FetchType.EAGER , optional = false)
    @JoinColumn(name = "Category_id")
    @JsonIgnore
    private Category category;

    public Book(String bookName, Category category) {
        this.bookName = bookName;
        this.category = category;
    }

    public Book(Long id , String bookName) {
        this.id = id;
        this.bookName = bookName;
    }
}
