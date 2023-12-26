package com.library.library.repository;

import com.library.library.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCategoryRepository extends JpaRepository<Category,Long> {
        Boolean existsByCategoryName(String categoryName);
}
