package ru.practicum.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "select * from categories " +
            "limit ?2 offset ?1 ", nativeQuery = true)
    public List<Category> getCats(Integer from, Integer size);
}
