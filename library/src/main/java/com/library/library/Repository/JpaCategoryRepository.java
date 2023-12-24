package com.library.library.Repository;

import com.library.library.Entity.Book;
import com.library.library.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCategoryRepository extends JpaRepository<Category,Long> {
        Boolean existsByCategoryName(String categoryName);
}
